package uk.co.ben_gibson.git.link.pipeline.middleware

import uk.co.ben_gibson.git.link.pipeline.Pass
import uk.co.ben_gibson.url.URL

interface Middleware : Comparable<Middleware> {
    val priority: Int

    operator fun invoke(pass: Pass, next: () -> URL?) : URL?

    override fun compareTo(other: Middleware): Int {
        return priority - other.priority
    }
}