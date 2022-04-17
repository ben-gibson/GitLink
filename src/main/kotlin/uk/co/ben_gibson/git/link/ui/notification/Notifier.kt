package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project

fun sendNotification(project : Project, notification : Notification) {
    val notificationManager = NotificationGroupManager
        .getInstance()
        .getNotificationGroup("GitLink Notification Group")

    val intellijNotification = notificationManager.createNotification(
        notification.title ?: "",
        notification.message,
        NotificationType.INFORMATION
    )

    notification.actions.forEach { action ->
        intellijNotification.addAction(DumbAwareAction.create(action.title) { action.run() })
    }

    intellijNotification.notify(project)
}