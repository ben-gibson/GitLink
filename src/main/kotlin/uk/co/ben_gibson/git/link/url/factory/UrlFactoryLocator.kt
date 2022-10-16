package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.platform.Azure
import uk.co.ben_gibson.git.link.platform.Chromium
import uk.co.ben_gibson.git.link.platform.Platform

@Service
class UrlFactoryLocator {
    fun locate(platform: Platform) : UrlFactory {
        return when(platform) {
            is Chromium -> service<ChromiumUrlFactory>()
            is Azure -> service<AzureUrlFactory>()
            else -> service<TemplatedUrlFactoryProvider>().forPlatform(platform)
        }
    }
}