package uk.co.ben_gibson.git.link.platform

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.Icons
import uk.co.ben_gibson.url.Host
import java.util.UUID

private val EXISTING_PLATFORMS = setOf(
    GitHub(),
    GitLab(),
    BitbucketCloud(),
    BitbucketServer(),
    Gitee(),
    Gitea(),
    Gogs(),
    Srht(),
    Azure(),
    Gerrit(),
    Codeberg()
)

@Service
class PlatformRepository {
    fun getById(id: String) = getById(UUID.fromString(id))
    fun getById(id: UUID) = load().firstOrNull { it.id == id }
    fun getAll() = load()

    /**
     * The platform serving a domain, e.g. github.com, searched for in the order:
     *
     * 1. A user registered domain against a platform, which is an explicit choice and so beats
     *    anything built-in.
     * 2. An exact match on a domain, so bitbucket.org resolves to Bitbucket Cloud
     *    before Bitbucket Server which uses '*bitbucket*'.
     * 3. A wildcard, e.g. '*gerrit*'.
     */
    fun getByDomain(host: Host): Platform? {
        val platforms = load()

        return getByRegisteredDomain(host)
            ?: platforms.firstOrNull { it.matchesExactly(host) }
            ?: platforms.firstOrNull { it.matches(host) }
    }

    private fun getByRegisteredDomain(host: Host) = service<ApplicationSettings>()
        .registeredDomains
        .entries
        .firstOrNull { (_, domains) -> domains.any { Domain.of(it).matches(host) } }
        ?.let { getById(it.key) }

    private fun load(): Set<Platform> {
        val settings = service<ApplicationSettings>()

        val customPlatforms: List<Platform> = settings.customPlatforms.map {
            Custom(
                UUID.fromString(it.id),
                it.displayName,
                Icons.GIT,
                setOf(Domain.of(it.baseUrl))
            )
        }

        return EXISTING_PLATFORMS.plus(customPlatforms)
    }
}
