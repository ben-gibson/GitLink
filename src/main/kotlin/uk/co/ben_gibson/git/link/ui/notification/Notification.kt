package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.GitLinkBundle.openPluginSettings
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.settings.ProjectSettings

data class Notification(val title: String? = null, val message: String, val actions: Set<NotificationAction> = setOf()) {

    companion object {
        private val DEFAULT_TITLE = message("name")

        fun hostNotSet(project: Project) = Notification(
            DEFAULT_TITLE,
            message("notifications.host-not-set"),
            actions = setOf(NotificationAction.settings(project))
        )

        fun repositoryNotFound() = Notification(DEFAULT_TITLE, message("notifications.repository-not-found"))

        fun remoteNotFound() = Notification(DEFAULT_TITLE, message("notifications.remote-not-found"))

        fun welcome(version: String) = Notification(message = message("notifications.welcome", version))

        fun performanceTips(project: Project) = Notification(
            message = message("notifications.performance"),
            actions = setOf(NotificationAction.disableCheckCommitOnRemote(project))
        )

        fun couldNotDetectGitHost(project: Project) = Notification(
            message = message("notifications.could-not-detect-host"),
            actions = setOf(NotificationAction.settings(project))
        )

        fun remoteHostAutoDetected(remoteHost: Host, project: Project) = Notification(
            message =  message("notifications.host-detected", remoteHost.displayName),
            actions = setOf(NotificationAction.settings(project))
        )
    }
}

data class NotificationAction(val title: String, val run: () -> Unit) {
    companion object {
        fun settings(project: Project) = NotificationAction(message("title.settings")) {
            openPluginSettings(project)
        }

        fun disableCheckCommitOnRemote(project: Project) = NotificationAction(message("actions.disable")) {
            project.service<ProjectSettings>().checkCommitOnRemote = false;
        }
    }
}
