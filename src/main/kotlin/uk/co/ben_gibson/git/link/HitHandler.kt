package uk.co.ben_gibson.git.link

import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification

fun handleHit() {
    val settings = service<ApplicationSettings>()

    settings.hits++

    if (settings.requestSupport && (settings.hits == 5 || settings.hits % 50 == 0)) {
        sendNotification(Notification.review())
    }
}