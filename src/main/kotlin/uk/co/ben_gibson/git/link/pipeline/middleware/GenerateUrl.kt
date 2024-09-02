package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import uk.co.ben_gibson.git.link.*
import uk.co.ben_gibson.git.link.git.*
import uk.co.ben_gibson.git.link.pipeline.Pass
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.url.UrlOptions
import uk.co.ben_gibson.git.link.url.factory.UrlFactoryLocator
import uk.co.ben_gibson.url.URL

// Must be the last middleware in the pipeline!
@Service
class GenerateUrl : Middleware {
    override val priority = 50

    override fun invoke(pass: Pass, next: () -> URL?) : URL? {
        // We can't reach this point unless the platform, repository, and remote have been resolved
        val baseUrl = pass.remoteOrThrow().httpUrl ?: return null

        val platform = pass.platformOrThrow()

        val options = createUrlOptions(pass, platform.pullRequestWorkflowSupported)

        return service<UrlFactoryLocator>().locate(platform).createUrl(baseUrl, options)
    }

    private fun createUrlOptions(pass: Pass, pullRequestWorkflowSupported: Boolean): UrlOptions {
        val remote = pass.remoteOrThrow()
        val repository = pass.repositoryOrThrow()
        val context = pass.context
        val settings = pass.project.service<ProjectSettings>()

        val repositoryFile = File.forRepository(context.file, repository)

        return when (context) {
            is ContextFileAtCommit -> UrlOptions.UrlOptionsFileAtCommit(
                repositoryFile,
                repository.currentBranch?.name ?: settings.fallbackBranch,
                context.commit,
                context.lineSelection
            )
            is ContextCommit -> UrlOptions.UrlOptionsCommit(
                context.commit,
                repository.currentBranch?.name ?: settings.fallbackBranch
            )
            is ContextCurrentFile -> {
                val commit = resolveCommit(repository, remote, settings, pullRequestWorkflowSupported)

                if (commit != null) {
                    UrlOptions.UrlOptionsFileAtCommit(
                        repositoryFile,
                        repository.currentBranch?.name ?: settings.fallbackBranch,
                        commit,
                        context.lineSelection
                    )
                } else {
                    UrlOptions.UrlOptionsFileAtBranch(
                        repositoryFile,
                        resolveBranch(repository, remote, settings),
                        context.lineSelection
                    )
                }
            }
        }
    }

    private fun resolveBranch(repository: GitRepository, remote: GitRemote, settings: ProjectSettings): String {
        val branch = repository.currentBranch ?: return settings.fallbackBranch

        if (!settings.shouldCheckRemote) {
            return branch.name
        }

        return if (remote.contains(repository, branch)) branch.name else settings.fallbackBranch
    }

    private fun resolveCommit(repository: GitRepository, remote: GitRemote, settings: ProjectSettings, pullRequestWorkflowSupported: Boolean): Commit? {
        val commit = repository.currentCommit() ?: return null

        if (!pullRequestWorkflowSupported || !settings.shouldCheckRemote) {
           return commit
        }

        return if (remote.contains(repository, commit)) commit else null
    }
}

