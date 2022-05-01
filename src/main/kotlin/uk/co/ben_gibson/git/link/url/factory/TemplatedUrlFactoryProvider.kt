package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.git.*
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import java.util.UUID

val HOST_MAP = mapOf(
    GitHub::class.java to UrlTemplates.gitHub(),
    GitLab::class.java to UrlTemplates.gitLab(),
    Gitee::class.java to UrlTemplates.gitee(),
    Gitea::class.java to UrlTemplates.gitea(),
    Gogs::class.java to UrlTemplates.gogs(),
    BitbucketServer::class.java to UrlTemplates.bitbucketServer(),
    BitbucketCloud::class.java to UrlTemplates.bitbucketCloud(),
    Azure::class.java to UrlTemplates.azure(),
)

@Service
class TemplatedUrlFactoryProvider() {
    fun forHost(host: Host): TemplatedUrlFactory {
        if (host is Custom) {
            return customHost(host)
        }

        return TemplatedUrlFactory(HOST_MAP.getValue(host::class.java))
    }

    private fun customHost(host: Custom): TemplatedUrlFactory {
        val config = service<ApplicationSettings>().customHosts.first { UUID.fromString(it.id).equals(host.id) }

        return TemplatedUrlFactory(UrlTemplates(config.fileAtBranchTemplate, config.fileAtCommitTemplate, config.commitTemplate))
    }
}