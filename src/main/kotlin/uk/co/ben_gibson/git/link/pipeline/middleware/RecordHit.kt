package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.pipeline.Pass
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.url.URL

@Service
class RecordHit : Middleware {
    override val priority = 20

    override fun invoke(pass: Pass, next: () -> URL?) : URL? {
        val url = next() ?: return null

        service<ApplicationSettings>().recordHit();

        return url;
    }
}

