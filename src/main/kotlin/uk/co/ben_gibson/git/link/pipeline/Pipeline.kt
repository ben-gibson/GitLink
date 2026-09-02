package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.git.locateRemote
import uk.co.ben_gibson.git.link.pipeline.middleware.*
import uk.co.ben_gibson.git.link.pipeline.middleware.Timer
import uk.co.ben_gibson.git.link.platform.Platform
import uk.co.ben_gibson.git.link.platform.PlatformLocator
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.Notifier
import uk.co.ben_gibson.url.URL

@Service(Service.Level.PROJECT)
class Pipeline(private val project: Project) {
    // Each middleware wraps the ones after it. GenerateUrl produces the URL rather than calling on, so it
    // must come last.
    private val middlewares: List<Middleware> = listOf(
        service<RequestSupport>(),
        service<RecordHit>(),
        service<ForceHttps>(),
        service<Timer>(),
        service<GenerateUrl>(),
    )

    fun accept(context: Context) : URL? {
        val repository = locateRepository(context) ?: return null
        val remote = locateRemote(repository) ?: return null
        val platform = locatePlatform() ?: return null

        val pass = Pass(project, context, platform, repository, remote)

        return next(middlewares, pass)
    }

    private fun next(remaining: List<Middleware>, pass: Pass) : URL? {
        val middleware = remaining.firstOrNull() ?: return null

        return middleware(pass) { next(remaining.drop(1), pass) }
    }

    private fun locatePlatform(): Platform? {
        val platform = project.service<PlatformLocator>().locate()

        if (platform == null) {
            service<Notifier>().send(Notification.platformNotSet(), project)
        }

        return platform
    }

    private fun locateRepository(context: Context): GitRepository? {
        val repository = GitRepositoryManager.getInstance(project).getRepositoryForFile(context.repositoryFile)

        repository ?: service<Notifier>().send(Notification.repositoryNotFound(), project)

        return repository
    }

    private fun locateRemote(repository: GitRepository): GitRemote? {
        val remote = repository.locateRemote(project.service<ProjectSettings>().remote)

        remote ?: service<Notifier>().send(Notification.remoteNotFound(), project)

        return remote
    }
}