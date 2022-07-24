package uk.co.ben_gibson.git.link.pipeline.middleware

import uk.co.ben_gibson.git.link.pipeline.Pass
import java.net.URI

interface Middleware : Comparable<Middleware> {
    val priority: Int

    operator fun invoke(pass: Pass, next: () -> URI?) : URI?

    override fun compareTo(other: Middleware): Int {
        return priority - other.priority
    }
}