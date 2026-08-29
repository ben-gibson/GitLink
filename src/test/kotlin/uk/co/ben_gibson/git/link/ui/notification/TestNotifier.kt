package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.openapi.project.Project

class TestNotifier : Notifier {
    private val sent = mutableListOf<Notification>()

    val notifications: List<Notification> get() = sent

    override fun send(notification: Notification, project: Project?) {
        sent.add(notification)
    }
}
