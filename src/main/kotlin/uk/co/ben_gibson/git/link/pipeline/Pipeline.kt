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
import java.util.*

@Service(Service.Level.PROJECT)
class Pipeline(private val project: Project) {
    private val middlewares: Set<Middleware> = setOf(
        service<GenerateUrl>(),
        service<Timer>(),
        service<RecordHit>(),
        service<ForceHttps>(),
        service<RequestSupport>(),
    )

    fun accept(context: Context) : URL? {
        if (middlewares.isEmpty()) {
            throw IllegalStateException("No middleware registered")
        }

        val repository = locateRepository(context) ?: return null
        val remote = locateRemote(repository) ?: return null
        val platform = locatePlatform() ?: return null

        val pass = Pass(project, context, platform, repository, remote)

        val queue = PriorityQueue(middlewares)

        return next(queue, pass)
    }

    private fun next(queue: PriorityQueue<Middleware>, pass: Pass) : URL? {
        val middleware = queue.remove()

        return middleware(pass) {
            return@middleware next(queue, pass)
        }
    }

    private fun locatePlatform(): Platform? {
        val platform = project.service<PlatformLocator>().locate()

        if (platform == null) {
            service<Notifier>().send(Notification.hostNotSet(), project)
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