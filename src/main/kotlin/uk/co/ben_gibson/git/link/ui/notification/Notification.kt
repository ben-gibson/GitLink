package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.GitLinkBundle.openPluginSettings
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.platform.Platform
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.url.URL

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
            message("notifications.platform-not-set"),
            actions = setOf(NotificationAction.settings(project))
        )

        fun repositoryNotFound() = Notification(DEFAULT_TITLE, message("notifications.repository-not-found"))

        fun remoteNotFound() = Notification(DEFAULT_TITLE, message("notifications.remote-not-found"))

        fun welcome(version: String) = Notification(message = message("notifications.welcome", version))

        fun star() = Notification(
            message = """
                Finding GitLink useful? Show your support 💖 and ⭐ the repository 🙏.
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

        fun platformPoll() = Notification(
            message = """
                Help improve GitLink by telling us which platform you use 🗳️.
            """.trimIndent(),
            actions = setOf(
                NotificationAction.openPlatformPoll() {
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

        fun couldNotDetectPlatform(project: Project) = Notification(
            message = message("notifications.could-not-detect-platform"),
            actions = setOf(NotificationAction.settings(project, message("actions.configure-manually")))
        )

        fun platformAutoDetected(remotePlatform: Platform, project: Project) = Notification(
            message =  message("notifications.platform-detected.message", remotePlatform.name),
            actions = setOf(NotificationAction.settings(project, message("notifications.platform-detected.action")))
        )

        fun linkCopied(link: URL) = Notification(
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
        fun openPlatformPoll(onComplete: () -> Unit) = NotificationAction(message("actions.sure-take-me-there")) { dismiss ->
            GitLinkBundle.openPlatformPoll()
            dismiss()
            onComplete()
        }

        fun doNotAskAgain(onComplete: () -> Unit) = NotificationAction(message("actions.do-not-ask-again")) { dismiss ->
            dismiss()
            onComplete()
        }

        fun openUrl(url: URL, title: String = message("actions.take-me-there")) = NotificationAction(title) { dismiss ->
            dismiss()
            BrowserLauncher.instance.open(url.toString());
        }

        fun disableCheckCommitOnRemote(project: Project) = NotificationAction(message("actions.disable")) { dismiss ->
            dismiss()
            project.service<ProjectSettings>().checkCommitOnRemote = false;
        }
    }
}
