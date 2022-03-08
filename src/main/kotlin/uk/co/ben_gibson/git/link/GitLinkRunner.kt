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
import uk.co.ben_gibson.git.link.url.Context
import uk.co.ben_gibson.git.link.url.UrlOptions
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

    val baseUrl = remote.httpUrl() ?: return
    val commit = context.commit ?: resolveCommit(repository, remote, settings)

    val url = urlFactory.createUrl(
        UrlOptions(
            baseUrl = baseUrl,
            file = context.file,
            commit = commit,
            branch = repository.currentBranch?.name ?: settings.defaultBranch,
            lineSelection = context.lineSelection
        )
    )

    handle(url)
}

private fun locateRepository(project: Project, file: VirtualFile): GitRepository? {
    val repository = findRepositoryForFile(project, file)

    if (repository == null) {
        sendNotification(project, Notification.repositoryNotFound())
    }

    return repository
}

private fun findRemote(project: Project, repository: GitRepository, settings: Settings): GitRemote? {
    val remote = repository.findRemote(settings.remote)

    if (remote == null) {
        sendNotification(project, Notification.remoteNotFound())
    }

    return remote
}

private fun resolveCommit(repository: GitRepository, remote: GitRemote, settings: Settings): Commit? {
    if (!settings.checkCommitOnRemote) {
        return null
    }

    val commit = repository.headCommit() ?: return null

    return commit.takeIf { repository.isCommitOnRemote(remote, it) }
}