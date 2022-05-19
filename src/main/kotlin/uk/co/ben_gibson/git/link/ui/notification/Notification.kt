package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.GitLinkBundle.openPluginSettings
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import java.net.URI

data class Notification(
    val title: String? = null,
    val message: String,
    val actions: Set<NotificationAction> = setOf(),
    val type: Type = Type.PERSISTENT
) {

    enum class Type {
        PERSISTENT,
        TRANSIENT
    }

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

        fun review() = Notification(
            message = """
                Finding GitLink useful? Show your support 💖, drop a review and ⭐ the repo 🙏.
            """.trimIndent(),
            actions = setOf(
                NotificationAction.openRepository() {
                    service<ApplicationSettings>().requestSupport = false;
                },
                NotificationAction.doNotAskAgain() {
                    service<ApplicationSettings>().requestSupport = false;
                }
            )
        )

        fun hostPoll() = Notification(
            message = """
                Help improve GitLink by telling us which host you use 🗳️.
            """.trimIndent(),
            actions = setOf(
                NotificationAction.openHostPoll() {
                    service<ApplicationSettings>().showHostPoll = false;
                },
                NotificationAction.doNotAskAgain() {
                    service<ApplicationSettings>().showHostPoll = false;
                }
            )
        )

        fun performanceTips(project: Project) = Notification(
            message = message("notifications.performance"),
            actions = setOf(
                NotificationAction.disableCheckCommitOnRemote(project),
                NotificationAction.doNotAskAgain { project.service<ProjectSettings>().showPerformanceTip = false },
            )
        )

        fun couldNotDetectGitHost(project: Project) = Notification(
            message = message("notifications.could-not-detect-host"),
            actions = setOf(NotificationAction.settings(project, message("actions.configure-manually")))
        )

        fun remoteHostAutoDetected(remoteHost: Host, project: Project) = Notification(
            message =  message("notifications.host-detected", remoteHost.displayName),
            actions = setOf(NotificationAction.settings(project, message("actions.configure-manually")))
        )

        fun linkCopied(link: URI) = Notification(
            DEFAULT_TITLE,
            message("notifications.copied-to-clipboard"),
            setOf(NotificationAction.openUrl(link)),
            Type.TRANSIENT,
        )
    }

    fun isTransient() = type == Type.TRANSIENT
    fun isPersistent() = !isTransient();
}

data class NotificationAction(val title: String, val run: (dismiss: () -> Unit) -> Unit) {
    companion object {
        fun settings(project: Project, title: String = message("title.settings")) = NotificationAction(title) { dismiss ->
            dismiss()
            openPluginSettings(project)
        }

        fun openRepository(onComplete: () -> Unit) = NotificationAction(message("actions.sure-take-me-there")) { dismiss ->
            GitLinkBundle.openRepository()
            dismiss()
            onComplete()
        }
        fun openHostPoll(onComplete: () -> Unit) = NotificationAction(message("actions.sure-take-me-there")) { dismiss ->
            GitLinkBundle.openHostPoll()
            dismiss()
            onComplete()
        }

        fun doNotAskAgain(onComplete: () -> Unit) = NotificationAction(message("actions.do-not-ask-again")) { dismiss ->
            dismiss()
            onComplete()
        }

        fun openUrl(url: URI, title: String = message("actions.take-me-there")) = NotificationAction(title) { dismiss ->
            dismiss()
            BrowserLauncher.instance.open(url.toString());
        }

        fun disableCheckCommitOnRemote(project: Project) = NotificationAction(message("actions.disable")) { dismiss ->
            dismiss()
            project.service<ProjectSettings>().checkCommitOnRemote = false;
        }
    }
}
