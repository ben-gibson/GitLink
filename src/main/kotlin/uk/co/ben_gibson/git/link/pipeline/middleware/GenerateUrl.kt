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
        val baseUrl = pass.remote.httpUrl ?: return null

        val options = createUrlOptions(pass)

        return service<UrlFactoryLocator>().locate(pass.platform).createUrl(baseUrl, options)
    }

    private fun createUrlOptions(pass: Pass): UrlOptions {
        val remote = pass.remote
        val repository = pass.repository
        val context = pass.context
        val settings = pass.project.service<ProjectSettings>()

        return when (context) {
            is Context.FileAtCommit -> UrlOptions.FileAtCommit(
                File.forRepository(context.file, repository),
                repository.currentBranch?.name ?: settings.fallbackBranch,
                context.commit,
                context.lineSelection
            )
            is Context.Commit -> UrlOptions.Commit(
                context.commit,
                repository.currentBranch?.name ?: settings.fallbackBranch
            )
            is Context.File -> {
                val commit = resolveCommit(repository, remote, settings, pass.platform.commitsReachableFromRemote)
                val repositoryFile = File.forRepository(context.file, repository)

                if (commit != null) {
                    UrlOptions.FileAtCommit(
                        repositoryFile,
                        repository.currentBranch?.name ?: settings.fallbackBranch,
                        commit,
                        context.lineSelection
                    )
                } else {
                    UrlOptions.FileAtBranch(
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

    private fun resolveCommit(repository: GitRepository, remote: GitRemote, settings: ProjectSettings, commitsReachableFromRemote: Boolean): Commit? {
        val commit = repository.currentCommit() ?: return null

        if (!commitsReachableFromRemote || !settings.shouldCheckRemote) {
           return commit
        }

        return if (remote.contains(repository, commit)) commit else null
    }
}

