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
    val hosts = project.service<HostsProvider>().provide()
    val host = hosts.getById(settings.host)

    val repository = locateRepository(project, context.file) ?: return
    val urlFactory = project.service<UrlFactoryLocator>().locate(host)

    val remote = findRemote(project, repository, settings) ?: return
    val remoteBaseUrl = remote.httpUrl() ?: return
    val repositoryFile = File.create(context.file, repository)

    val urlOptions = when(context) {
        is ContextFileAtCommit -> UrlOptionsFileAtCommit(remoteBaseUrl, repositoryFile, context.commit, context.lineSelection)
        is ContextFileAtBranch -> UrlOptionsFileAtBranch(remoteBaseUrl, repositoryFile, context.branch, context.lineSelection)
        is ContextCommit -> UrlOptionsCommit(remoteBaseUrl, context.commit)
        is ContextCurrentFile -> {
            val commit = resolveCommit(repository, remote, settings)

            if (commit != null) {
                UrlOptionsFileAtCommit(remoteBaseUrl, repositoryFile, commit, context.lineSelection)
            } else {
                UrlOptionsFileAtBranch(
                    remoteBaseUrl,
                    repositoryFile,
                    resolveBranch(repository, remote, settings),
                    context.lineSelection
                )
            }
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

private fun resolveBranch(repository: GitRepository, remote: GitRemote, settings: Settings): String {
    val branch = repository.currentBranch

    if (branch != null && remote.contains(repository, branch)) {
        return branch.name
    }

    return settings.fallbackBranch
}

private fun resolveCommit(repository: GitRepository, remote: GitRemote, settings: Settings): Commit? {
    val commit = repository.currentCommit() ?: return null

    return if (settings.checkCommitOnRemote && remote.contains(repository, commit)) commit else null
}