package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import java.net.URL
import uk.co.ben_gibson.git.link.Context

@Service
class RecordHitMiddleware : Middleware {
    override val priority = 2

    override fun invoke(project: Project, context: Context, next: () -> URL?) : URL? {
        val url = next() ?: return null

        service<ApplicationSettings>().incrementHits();

        return url;
    }
}

