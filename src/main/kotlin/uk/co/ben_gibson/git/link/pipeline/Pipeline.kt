package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.Context
import java.net.URI
import java.util.*
import kotlin.collections.Set

@Service
class Pipeline(private val project: Project) {
    private val middlewares: Set<Middleware> = setOf(
        project.service<GenerateUrlMiddleware>(),
        project.service<TimerMiddleware>(),
        project.service<RecordHitMiddleware>(),
        project.service<ForceHttpsMiddleware>(),
        project.service<RatePluginMiddleware>(),
        project.service<HostPollMiddleware>(),
        project.service<ResolveContextMiddleware>(),
    )

    fun accept(context: Context) : URI? {
        if (middlewares.isEmpty()) {
            throw IllegalStateException("No middleware registered")
        }

        val queue = PriorityQueue<Middleware>(middlewares)

        return next(queue, Pass(project, context))
    }

    private fun next(queue: PriorityQueue<Middleware>, pass: Pass) : URI? {
        val middleware = queue.remove()

        return middleware(pass) {
            return@middleware next(queue, pass);
        }
    }
}