package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.Icons
import java.net.URI
import java.util.*

private val HOSTS = setOf(
    GitHub(),
    GitLab(),
    BitbucketCloud(),
    BitbucketServer(),
    Gitee(),
    Gitea(),
    Gogs(),
    Azure(),
    Chromium()
)

@Service
class HostsProvider {
    fun provide(): Hosts {
        val settings = service<ApplicationSettings>()

        val customHosts: List<Host> = settings.customHosts.map {
            Custom(
                UUID.fromString(it.id),
                it.displayName,
                Icons.GIT,
                setOf(URI(it.baseUrl))
            )
        }

        return Hosts(HOSTS.plus(customHosts))
    }
}