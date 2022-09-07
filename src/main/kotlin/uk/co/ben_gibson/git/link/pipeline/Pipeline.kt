package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.pipeline.middleware.*
import uk.co.ben_gibson.git.link.pipeline.middleware.Timer
import uk.co.ben_gibson.url.URL
import java.util.*
import kotlin.collections.Set

@Service
class Pipeline(private val project: Project) {
    private val middlewares: Set<Middleware> = setOf(
        project.service<GenerateUrl>(),
        project.service<Timer>(),
        project.service<RecordHit>(),
        project.service<ForceHttps>(),
        project.service<SendSupportNotification>(),
        project.service<SendPollNotification>(),
        project.service<ResolveContext>(),
    )

    fun accept(context: Context) : URL? {
        if (middlewares.isEmpty()) {
            throw IllegalStateException("No middleware registered")
        }

        val queue = PriorityQueue(middlewares)

        return next(queue, Pass(project, context))
    }

    private fun next(queue: PriorityQueue<Middleware>, pass: Pass) : URL? {
        val middleware = queue.remove()

        return middleware(pass) {
            return@middleware next(queue, pass);
        }
    }
}