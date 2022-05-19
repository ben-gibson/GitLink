package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.git.*

@Service
class UrlFactoryLocator {
    fun locate(host: Host) : UrlFactory {
        return when(host) {
            is Chromium -> service<ChromiumUrlFactory>()
            else -> service<TemplatedUrlFactoryProvider>().forHost(host)
        }
    }
}