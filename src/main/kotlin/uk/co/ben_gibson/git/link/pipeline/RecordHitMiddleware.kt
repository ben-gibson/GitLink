package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import java.net.URL

@Service
class RecordHitMiddleware : Middleware {
    override val priority = 20

    override fun invoke(pass: Pass, next: () -> URL?) : URL? {
        val url = next() ?: return null

        service<ApplicationSettings>().incrementHits();

        return url;
    }
}

