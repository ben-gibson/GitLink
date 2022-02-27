package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service
class Notifier {
    private val notificationGroup = NotificationGroupManager
        .getInstance()
        .getNotificationGroup("GitLink Notification Group")

    fun sendNotification(project : Project, notification : Notification) {
        notificationGroup.createNotification(
            notification.title,
            notification.message,
            NotificationType.INFORMATION
        )
            .notify(project)
    }
}