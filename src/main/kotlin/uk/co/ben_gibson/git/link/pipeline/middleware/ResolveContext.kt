package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager
import uk.co.ben_gibson.git.link.git.*
import uk.co.ben_gibson.git.link.pipeline.Pass
import uk.co.ben_gibson.git.link.platform.Platform
import uk.co.ben_gibson.git.link.platform.PlatformLocator
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification
import uk.co.ben_gibson.url.URL

@Service
class ResolveContext : Middleware {
    override val priority = 5

    override fun invoke(pass: Pass, next: () -> URL?): URL? {
        val repository = locateRepository(pass) ?: return null
        val remote = locateRemote(pass, repository) ?: return null
        val platform = localePlatform(pass) ?: return null

        pass.platform = platform
        pass.repository = repository
        pass.remote = remote

        return next()
    }

    private fun localePlatform(pass: Pass): Platform? {
        val platform = pass.project.service<PlatformLocator>().locate()

        if (platform == null) {
            sendNotification(Notification.hostNotSet(pass.project), pass.project)
        }

        return platform
    }

    private fun locateRepository(pass: Pass): GitRepository? {
        val repository = GitRepositoryManager.getInstance(pass.project).getRepositoryForFile(pass.context.file)

        repository ?: sendNotification(Notification.repositoryNotFound(), pass.project)

        return repository
    }

    private fun locateRemote(pass: Pass, repository: GitRepository): GitRemote? {
        val remote = repository.locateRemote(pass.project.service<ProjectSettings>().remote)

        remote ?: sendNotification(Notification.remoteNotFound(), pass.project)

        return remote
    }
}