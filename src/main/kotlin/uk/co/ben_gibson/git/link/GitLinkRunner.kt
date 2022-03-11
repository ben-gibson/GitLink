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
    processGitLink(
        project,
        context
    ) { Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(it.toString()), null) }
}

private fun processGitLink(project: Project, context: Context, handle: (URL) -> Unit) {
    runBackgroundableTask("Foo", project, false) {
        val (_, totalTime) = timeOperation { process(project, context, handle) }

        if (totalTime > 1) {
            sendNotification(project, Notification.performanceTips())
        }
    }
}

private fun process(project: Project, context: Context, handle: (URL) -> Unit) {
    val settings = project.service<Settings>()

    val repository = locateRepository(project, context.file) ?: return
    val urlFactory = project.service<UrlFactoryLocator>().locateFactory(settings.remoteHost)

    val remote = findRemote(project, repository, settings) ?: return
    val remoteBaseUrl = remote.httpUrl() ?: return
    val repositoryFile = File.create(context.file.name, repository.getRelativeFilePath(context.file))

    val urlOptions = when(context) {
        is ContextFileAtCommit -> UrlOptionsFileAtCommit(
            baseUrl = remoteBaseUrl,
            file = repositoryFile,
            commit = context.commit,
            lineSelection = context.lineSelection
        )
        is ContextFileAtBranch -> UrlOptionsFileAtBranch(
            baseUrl = remoteBaseUrl,
            file = repositoryFile,
            branch = context.branch,
            lineSelection = context.lineSelection
        )
        is ContextCommit -> UrlOptionsCommit(
            baseUrl = remoteBaseUrl,
            commit = context.commit,
            lineSelection = context.lineSelection
        )
        is ContextCurrentFile -> {
            val commit = attemptToResolveCurrentCommit(repository, remote, settings)

            if (commit != null) {
                UrlOptionsFileAtCommit(
                    baseUrl = remoteBaseUrl,
                    file = repositoryFile,
                    commit = commit,
                    lineSelection = context.lineSelection
                )
            }

            UrlOptionsFileAtBranch(
                baseUrl = remoteBaseUrl,
                file = repositoryFile,
                branch = repository.currentBranch?.name ?: settings.defaultBranch,
                lineSelection = context.lineSelection
            )
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

private fun attemptToResolveCurrentCommit(repository: GitRepository, remote: GitRemote, settings: Settings): Commit? {
    if(settings.checkCommitOnRemote) {
        return null
    }

    return repository.headCommit()?.takeIf { repository.isCommitOnRemote(remote, it) }
}