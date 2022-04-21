package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.url.toHttps
import java.net.URL
import uk.co.ben_gibson.git.link.Context

@Service
class ForceHttpsMiddleware : Middleware {
    override val priority = 3

    override fun invoke(project: Project, context: Context, next: () -> URL?) : URL? {
        val url = next() ?: return null

        val settings = project.service<ProjectSettings>()

        if (settings.forceHttps) {
            return url.toHttps()
        }

        return url;
    }
}

