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
import uk.co.ben_gibson.git.link.url.UrlOptionsCommit
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtBranch
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtCommit
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

        val urlOptions = createUrlOptions(pass, baseUrl)

        return service<UrlFactoryLocator>().locate(platform).createUrl(urlOptions)
    }

    private fun createUrlOptions(pass: Pass, baseUrl: URL): UrlOptions {
        val remote = pass.remoteOrThrow()
        val repository = pass.repositoryOrThrow()
        val context = pass.context
        val settings = pass.project.service<ProjectSettings>()

        val repositoryFile = File.forRepository(context.file, repository)

        return when (context) {
            is ContextFileAtCommit -> UrlOptionsFileAtCommit(baseUrl, repositoryFile, context.commit, context.lineSelection)
            is ContextCommit -> UrlOptionsCommit(baseUrl, context.commit)
            is ContextCurrentFile -> {
                val commit = resolveCommit(repository, remote, settings)

                if (commit != null) {
                    UrlOptionsFileAtCommit(baseUrl, repositoryFile, commit, context.lineSelection)
                } else {
                    UrlOptionsFileAtBranch(
                        baseUrl,
                        repositoryFile,
                        resolveBranch(repository, remote, settings),
                        context.lineSelection
                    )
                }
            }
        }
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

