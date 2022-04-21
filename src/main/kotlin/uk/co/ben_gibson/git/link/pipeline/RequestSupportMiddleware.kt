package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification
import java.net.URL
import uk.co.ben_gibson.git.link.Context

@Service
class RequestSupportMiddleware : Middleware {
    override val priority = 1

    override fun invoke(project: Project, context: Context, next: () -> URL?) : URL? {
        val url = next()

        val settings = service<ApplicationSettings>()

        if (settings.requestSupport && (settings.hits == 5 || settings.hits % 50 == 0)) {
            sendNotification(Notification.review())
        }

        return url;
    }
}

