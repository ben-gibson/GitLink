package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.platform.*

@Service
class UrlFactoryLocator {
    fun locate(platform: Platform) : UrlFactory {
        return when(platform) {
            is Azure -> service<AzureUrlFactory>()
            is BitbucketServer -> service<BitbucketServerUrlFactory>()
            is TemplatedPlatform -> TemplatedUrlFactory(platform.templates)
        }
    }
}
