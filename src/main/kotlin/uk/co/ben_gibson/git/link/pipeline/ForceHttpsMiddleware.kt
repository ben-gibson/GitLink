package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.url.toHttps
import java.net.URI

@Service
class ForceHttpsMiddleware : Middleware {
    override val priority = 30

    override fun invoke(pass: Pass, next: () -> URI?) : URI? {
        val url = next() ?: return null

        val settings = pass.project.service<ProjectSettings>()

        if (settings.forceHttps) {
            return url.toHttps()
        }

        return url;
    }
}

