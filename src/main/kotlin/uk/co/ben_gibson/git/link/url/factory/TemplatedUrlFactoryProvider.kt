package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.platform.*
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import java.util.UUID

val PLATFORM_MAP = mapOf(
    GitHub::class.java to UrlTemplates.gitHub(),
    GitLab::class.java to UrlTemplates.gitLab(),
    Gitee::class.java to UrlTemplates.gitee(),
    Gitea::class.java to UrlTemplates.gitea(),
    Gogs::class.java to UrlTemplates.gogs(),
    BitbucketServer::class.java to UrlTemplates.bitbucketServer(),
    BitbucketCloud::class.java to UrlTemplates.bitbucketCloud(),
    Gerrit::class.java to UrlTemplates.gerrit(),
)

@Service
class TemplatedUrlFactoryProvider() {
    fun forPlatform(platform: Platform): TemplatedUrlFactory {
        if (platform is Custom) {
            return customPlatform(platform)
        }

        return TemplatedUrlFactory(PLATFORM_MAP.getValue(platform::class.java))
    }

    private fun customPlatform(host: Custom): TemplatedUrlFactory {
        val config = service<ApplicationSettings>().customHosts.first { UUID.fromString(it.id).equals(host.id) }

        return TemplatedUrlFactory(UrlTemplates(config.fileAtBranchTemplate, config.fileAtCommitTemplate, config.commitTemplate))
    }
}
