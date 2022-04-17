package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.git.TemplatedHost

@Service
class UrlFactoryLocator {
    fun locate(host: Host) : UrlFactory {
        // All hosts currently use templates, but they don't have to be. For example,
        // we could easily add a dedicated URL factory for more complex cases e.g. GitHubUrlFactory.
        return when(host) {
            is TemplatedHost -> TemplatedUrlFactory(host.urlTemplate)
        }
    }
}