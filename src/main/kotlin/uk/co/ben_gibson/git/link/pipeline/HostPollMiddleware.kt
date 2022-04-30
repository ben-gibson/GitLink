package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification
import java.net.URL

@Service
class HostPollMiddleware : Middleware {
    override val priority = 10

    override fun invoke(pass: Pass, next: () -> URL?) : URL? {
        val url = next()

        val settings = service<ApplicationSettings>()

        if (settings.showHostPoll && settings.hits % 10 == 0) {
            sendNotification(Notification.hostPoll())
        }

        return url;
    }
}

