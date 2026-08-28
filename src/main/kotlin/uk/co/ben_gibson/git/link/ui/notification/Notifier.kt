package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.GitLinkBundle.openPluginSettings
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.Icons.GIT_LINK

private const val IMPORTANT_GROUP_ID = "git.link.notification.important"
private const val GENERAL_GROUP_ID = "git.link.notification.general"

@Service
open class Notifier {
    open fun send(notification : Notification, project : Project? = null) {
        val groupId = when(notification.type) {
            Notification.Type.PERSISTENT -> IMPORTANT_GROUP_ID
            Notification.Type.TRANSIENT -> GENERAL_GROUP_ID
        }

        val notificationManager = NotificationGroupManager
            .getInstance()
            .getNotificationGroup(groupId)

        val intellijNotification = notificationManager.createNotification(
            notification.title ?: "",
            notification.message,
            NotificationType.INFORMATION
        )

        intellijNotification.icon = GIT_LINK

        notification.actions.forEach { action ->
            intellijNotification.addAction(DumbAwareAction.create(action.title) {
                intellijNotification.expire()
                perform(action, project)
            })
        }

        intellijNotification.notify(project)
    }

    private fun perform(action: NotificationAction, project: Project?) {
        when (action) {
            is NotificationAction.OpenSettings -> project?.let { openPluginSettings(it) }
            is NotificationAction.OpenUrl -> BrowserLauncher.instance.open(action.url.toString())
            is NotificationAction.OpenRepository -> {
                GitLinkBundle.openRepository()
                disable(Setting.SUPPORT_REQUEST, project)
            }
            is NotificationAction.DisableSetting -> disable(action.setting, project)
        }
    }

    private fun disable(setting: Setting, project: Project?) {
        when (setting) {
            Setting.SUPPORT_REQUEST -> service<ApplicationSettings>().requestSupport = false
            Setting.PERFORMANCE_TIP -> project?.service<ProjectSettings>()?.showPerformanceTip = false
            Setting.REMOTE_CHECK -> project?.service<ProjectSettings>()?.shouldCheckRemote = false
        }
    }
}
