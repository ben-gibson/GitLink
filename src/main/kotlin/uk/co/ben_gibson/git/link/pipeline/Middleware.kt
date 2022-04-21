package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.Context
import java.net.URL

interface Middleware : Comparable<Middleware> {
    val priority: Int

    operator fun invoke(project: Project, context: Context, next: () -> URL?) : URL?

    override fun compareTo(other: Middleware): Int {
        return priority - other.priority
    }
}