package uk.co.ben_gibson.git.link

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import uk.co.ben_gibson.git.link.git.*
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification
import uk.co.ben_gibson.git.link.url.UrlOptionsCommit
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtBranch
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtCommit
import uk.co.ben_gibson.git.link.url.factory.UrlFactoryLocator
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URL

fun openInBrowser(project: Project, context: Context) {
    processGitLink(project, context) { BrowserUtil.browse(it) }
}

fun copyToClipBoard(project: Project, context: Context) {
    processGitLink(project, context) {
        Toolkit.getDefaultToolkit().systemClipboard.setContents(
            StringSelection(it.toString()),
            null
        )
    }
}

private fun processGitLink(project: Project, context: Context, handle: (URL) -> Unit) {
    runBackgroundableTask("Foo", project, false) {
        val time = timeOperation { process(project, context, handle) }

        if (time < 1000) {
            return@runBackgroundableTask
        }

        sendNotification(project, Notification.performanceTips())
    }
}

private fun process(project: Project, context: Context, handle: (URL) -> Unit) {
    val settings = project.service<Settings>()

    val repository = locateRepository(project, context.file) ?: return
    val urlFactory = project.service<UrlFactoryLocator>().locate(settings.host)

    val remote = findRemote(project, repository, settings) ?: return
    val remoteBaseUrl = remote.httpUrl() ?: return
    val repositoryFile = File.create(context.file.name, repository.getRelativeFilePath(context.file))

    val urlOptions = when(context) {
        is ContextFileAtCommit -> UrlOptionsFileAtCommit(remoteBaseUrl, repositoryFile, context.commit, context.lineSelection)
        is ContextFileAtBranch -> UrlOptionsFileAtBranch(remoteBaseUrl, repositoryFile, context.branch, context.lineSelection)
        is ContextCommit -> UrlOptionsCommit(remoteBaseUrl, context.commit, context.lineSelection)
        is ContextCurrentFile -> {
            val commit = repository.currentCommit()

            if (commit != null && settings.checkCommitOnRemote && repository.isCommitOnRemote(remote, commit)) {
                UrlOptionsFileAtCommit(remoteBaseUrl, repositoryFile, commit, context.lineSelection)
            }

            UrlOptionsFileAtBranch(remoteBaseUrl, repositoryFile, repository.currentBranch(settings.defaultBranch), context.lineSelection)
        }
    }

    handle(urlFactory.createUrl(urlOptions))
}

private fun locateRepository(project: Project, file: VirtualFile): GitRepository? {
    val repository = findRepositoryForFile(project, file)

    repository ?: sendNotification(project, Notification.repositoryNotFound())

    return repository
}

private fun findRemote(project: Project, repository: GitRepository, settings: Settings): GitRemote? {
    val remote = repository.findRemote(settings.remote)

    remote ?: sendNotification(project, Notification.remoteNotFound())

    return remote
}