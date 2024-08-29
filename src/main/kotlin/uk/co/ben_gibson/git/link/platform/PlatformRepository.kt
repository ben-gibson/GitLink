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
    Chromium(),
    Gerrit()
)

@Service
class PlatformRepository {
    fun getById(id: String) = getById(UUID.fromString(id))
    fun getById(id: UUID) = load().firstOrNull { it.id == id }
    fun getByDomain(domain: Host): Platform? {
        val platforms = load()
        return platforms.firstOrNull { it.domains.contains(domain) } ?: platforms.firstOrNull { it.domainPattern?.matcher(domain.toString())?.matches() == true }
    }
    fun getAll() = load()

    private fun load(): Set<Platform> {
        val settings = service<ApplicationSettings>()

        val customPlatforms: List<Platform> = settings.customHosts.map {
            Custom(
                UUID.fromString(it.id),
                it.displayName,
                Icons.GIT,
                setOf(Host(it.baseUrl))
            )
        }

        return EXISTING_PLATFORMS.plus(customPlatforms)
    }
}
