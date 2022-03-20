package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project

fun sendNotification(project : Project, notification : Notification) {
    val notificationManager = NotificationGroupManager
        .getInstance()
        .getNotificationGroup("GitLink Notification Group")

    val intellijNotification = notificationManager.createNotification(
        notification.title,
        notification.message,
        NotificationType.INFORMATION
    ).addAction(
        DumbAwareAction.create("Settings") { _: AnActionEvent? ->
            ShowSettingsUtil.getInstance().showSettingsDialog(project, "GitLink.Settings")
        }
    )

    intellijNotification.notify(project)
}