package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.git.ChromiumHost
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.git.TemplatedHost

@Service
class UrlFactoryLocator {
    fun locate(host: Host) : UrlFactory {
        return when(host) {
            is TemplatedHost -> TemplatedUrlFactory(host.urlTemplate)
            is ChromiumHost -> service<ChromiumUrlFactory>()
        }
    }
}