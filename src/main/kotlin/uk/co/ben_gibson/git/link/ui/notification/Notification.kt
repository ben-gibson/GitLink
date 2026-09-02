package uk.co.ben_gibson.git.link.ui.notification

import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.platform.Platform
import uk.co.ben_gibson.url.URL

data class Notification(
    val title: String? = null,
    val message: String,
    val actions: List<NotificationAction> = listOf(),
    val type: Type = Type.PERSISTENT
) {
    enum class Type {
        PERSISTENT,
        TRANSIENT
    }

    companion object {
        private val DEFAULT_TITLE = message("name")

        fun platformNotSet() = Notification(
            DEFAULT_TITLE,
            message("notifications.platform-not-set"),
            actions = listOf(NotificationAction.OpenSettings())
        )

        fun repositoryNotFound() = Notification(DEFAULT_TITLE, message("notifications.repository-not-found"))

        fun remoteNotFound() = Notification(DEFAULT_TITLE, message("notifications.remote-not-found"))

        fun welcome(version: String) = Notification(message = message("notifications.welcome", version))

        fun star() = Notification(
            message = """
                Finding GitLink useful? Show your support 💖 and ⭐ the repository 🙏.
            """.trimIndent(),
            actions = listOf(
                NotificationAction.OpenRepository(),
                NotificationAction.DisableSetting(Setting.SUPPORT_REQUEST, message("actions.do-not-ask-again"))
            )
        )

        fun performanceTips() = Notification(
            message = message("notifications.performance"),
            actions = listOf(
                NotificationAction.DisableSetting(Setting.REMOTE_CHECK, message("actions.disable")),
                NotificationAction.DisableSetting(Setting.PERFORMANCE_TIP, message("actions.do-not-ask-again"))
            )
        )

        fun couldNotDetectPlatform() = Notification(
            message = message("notifications.could-not-detect-platform"),
            actions = listOf(NotificationAction.OpenSettings(message("actions.configure-manually")))
        )

        fun platformAutoDetected(remotePlatform: Platform) = Notification(
            message =  message("notifications.platform-detected.message", remotePlatform.name),
            actions = listOf(NotificationAction.OpenSettings(message("notifications.platform-detected.action")))
        )

        fun linkCopied(link: URL) = Notification(
            DEFAULT_TITLE,
            message("notifications.copied-to-clipboard"),
            listOf(NotificationAction.OpenUrl(link)),
            Type.TRANSIENT,
        )
    }
}

enum class Setting {
    SUPPORT_REQUEST,
    PERFORMANCE_TIP,
    REMOTE_CHECK
}

sealed class NotificationAction {
    abstract val title: String

    data class OpenSettings(
        override val title: String = message("title.settings")
    ) : NotificationAction()

    data class OpenUrl(
        val url: URL,
        override val title: String = message("actions.take-me-there")
    ) : NotificationAction()

    // Opening the repository is taken as the support having been given, so the request is not shown again.
    data class OpenRepository(
        override val title: String = message("actions.sure-take-me-there")
    ) : NotificationAction()

    data class DisableSetting(
        val setting: Setting,
        override val title: String
    ) : NotificationAction()
}
