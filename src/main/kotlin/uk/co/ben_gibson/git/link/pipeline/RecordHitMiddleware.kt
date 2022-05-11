package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import java.net.URI

@Service
class RecordHitMiddleware : Middleware {
    override val priority = 20

    override fun invoke(pass: Pass, next: () -> URI?) : URI? {
        val url = next() ?: return null

        service<ApplicationSettings>().recordHit();

        return url;
    }
}

