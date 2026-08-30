package uk.co.ben_gibson.git.link.platform

import uk.co.ben_gibson.url.Host
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * A domain a platform is served from, either a host or a wildcard matching any host containing a
 * fragment, e.g. a wildcard of 'gerrit' matches 'gerrit.example.com'. Self hosted platforms rely on
 * the wildcard, as they have no domain of their own to match.
 */
class Domain private constructor(private val value: String, val isWildcard: Boolean) {
    companion object {
        fun of(host: String) = Domain(host, false)

        fun wildcard(fragment: String) = Domain(fragment, true)
    }

    private val pattern = Regex.escape(value)
        .let { if (isWildcard) ".*$it.*" else it }
        .toRegex(IGNORE_CASE)

    fun matches(host: Host) = pattern.matches(host.toString())

    override fun toString() = if (isWildcard) "*$value*" else value

    override fun equals(other: Any?) = other is Domain && toString().equals(other.toString(), ignoreCase = true)

    override fun hashCode() = toString().lowercase().hashCode()
}
