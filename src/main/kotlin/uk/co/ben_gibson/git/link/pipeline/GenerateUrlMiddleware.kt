package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import uk.co.ben_gibson.git.link.*
import uk.co.ben_gibson.git.link.git.*
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification
import uk.co.ben_gibson.git.link.url.UrlOptionsCommit
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtBranch
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtCommit
import uk.co.ben_gibson.git.link.url.factory.UrlFactoryLocator
import java.net.URL

// Must be the last middleware in the pipeline!
@Service
class GenerateUrlMiddleware : Middleware {
    override val priority = 5

    override fun invoke(project: Project, context: Context, next: () -> URL?) : URL? {
        val settings = project.service<ProjectSettings>()

        val host = project.service<HostLocator>().locate()

        if (host == null) {
            sendNotification(Notification.hostNotSet(project), project)
            return null
        }

        val repository = locateRepository(project, context.file) ?: return null
        val urlFactory = project.service<UrlFactoryLocator>().locate(host)

        val remote = findRemote(project, repository, settings) ?: return null
        val remoteBaseUrl = remote.httpUrl() ?: return null
        val repositoryFile = File.forRepository(context.file, repository)

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

        return urlFactory.createUrl(urlOptions)
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
}

