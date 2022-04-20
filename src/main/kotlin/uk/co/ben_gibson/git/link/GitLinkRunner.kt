package uk.co.ben_gibson.git.link

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.git.*
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification
import uk.co.ben_gibson.git.link.url.UrlOptionsCommit
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtBranch
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtCommit
import uk.co.ben_gibson.git.link.url.factory.UrlFactoryLocator
import uk.co.ben_gibson.git.link.url.toHttps
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
        sendNotification(Notification.linkCopied(), project)
    }
}

private fun processGitLink(project: Project, context: Context, handle: (URL) -> Unit) {
    runBackgroundableTask(message("name"), project, false) {
        val time = timeOperation { process(project, context, handle) }

        if (time < 1000 || !project.service<ProjectSettings>().checkCommitOnRemote) {
            return@runBackgroundableTask
        }

        sendNotification(Notification.performanceTips(project), project)
    }
}

private fun process(project: Project, context: Context, handle: (URL) -> Unit) {
    val settings = project.service<ProjectSettings>()

    val host = project.service<HostLocator>().locate()

    if (host == null) {
        sendNotification(Notification.hostNotSet(project), project)
        return
    }

    val repository = locateRepository(project, context.file) ?: return
    val urlFactory = project.service<UrlFactoryLocator>().locate(host)

    val remote = findRemote(project, repository, settings) ?: return
    var remoteBaseUrl = remote.httpUrl() ?: return
    val repositoryFile = File.forRepository(context.file, repository)

    if (settings.forceHttps) {
        remoteBaseUrl = remoteBaseUrl.toHttps()
    }

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

    handleHit();
}

private fun locateRepository(project: Project, file: VirtualFile): GitRepository? {
    val repository = findRepositoryForFile(project, file)

    repository ?: sendNotification(Notification.repositoryNotFound(), project)

    return repository
}

private fun findRemote(project: Project, repository: GitRepository, settings: ProjectSettings): GitRemote? {
    val remote = repository.findRemote(settings.remote)

    remote ?: sendNotification(Notification.remoteNotFound(), project)

    return remote
}

private fun resolveBranch(repository: GitRepository, remote: GitRemote, settings: ProjectSettings): String {
    val branch = repository.currentBranch

    if (branch != null && remote.contains(repository, branch)) {
        return branch.name
    }

    return settings.fallbackBranch
}

private fun resolveCommit(repository: GitRepository, remote: GitRemote, settings: ProjectSettings): Commit? {
    val commit = repository.currentCommit() ?: return null

    return if (settings.checkCommitOnRemote && remote.contains(repository, commit)) commit else null
}