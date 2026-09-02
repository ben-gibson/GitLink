package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.testFramework.junit5.TestApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.platform.GitHub
import uk.co.ben_gibson.url.URL

@TestApplication
class NotificationTest {

    @Test
    fun `can create platform not set notification`() {
        val notification = Notification.platformNotSet()

        assertThat(notification.title).isEqualTo(message("name"))
        assertThat(notification.message).isEqualTo(message("notifications.platform-not-set"))
        assertThat(notification.type).isEqualTo(Notification.Type.PERSISTENT)
        assertThat(notification.actions).containsExactly(NotificationAction.OpenSettings())
    }

    @Test
    fun `can create repository not found notification`() {
        val notification = Notification.repositoryNotFound()

        assertThat(notification.title).isEqualTo(message("name"))
        assertThat(notification.message).isEqualTo(message("notifications.repository-not-found"))
        assertThat(notification.type).isEqualTo(Notification.Type.PERSISTENT)
        assertThat(notification.actions).isEmpty()
    }

    @Test
    fun `can create remote not found notification`() {
        val notification = Notification.remoteNotFound()

        assertThat(notification.title).isEqualTo(message("name"))
        assertThat(notification.message).isEqualTo(message("notifications.remote-not-found"))
        assertThat(notification.type).isEqualTo(Notification.Type.PERSISTENT)
        assertThat(notification.actions).isEmpty()
    }

    @Test
    fun `can create welcome notification`() {
        val notification = Notification.welcome("4.5.5")

        assertThat(notification.title).isNull()
        assertThat(notification.message).isEqualTo(message("notifications.welcome", "4.5.5"))
        assertThat(notification.message).contains("4.5.5")
        assertThat(notification.type).isEqualTo(Notification.Type.PERSISTENT)
        assertThat(notification.actions).isEmpty()
    }

    @Test
    fun `can create star notification`() {
        val notification = Notification.star()

        assertThat(notification.title).isNull()
        assertThat(notification.message).contains("GitLink")
        assertThat(notification.type).isEqualTo(Notification.Type.PERSISTENT)
        assertThat(notification.actions).containsExactly(
            NotificationAction.OpenRepository(),
            NotificationAction.OpenReview(),
            NotificationAction.DisableSetting(Setting.SUPPORT_REQUEST, message("actions.do-not-ask-again"))
        )
    }

    @Test
    fun `can create performance tips notification`() {
        val notification = Notification.performanceTips()

        assertThat(notification.title).isNull()
        assertThat(notification.message).isEqualTo(message("notifications.performance"))
        assertThat(notification.type).isEqualTo(Notification.Type.PERSISTENT)
        assertThat(notification.actions).containsExactly(
            NotificationAction.DisableSetting(Setting.REMOTE_CHECK, message("actions.disable")),
            NotificationAction.DisableSetting(Setting.PERFORMANCE_TIP, message("actions.do-not-ask-again"))
        )
    }

    @Test
    fun `can create could not detect platform notification`() {
        val notification = Notification.couldNotDetectPlatform()

        assertThat(notification.title).isNull()
        assertThat(notification.message).isEqualTo(message("notifications.could-not-detect-platform"))
        assertThat(notification.type).isEqualTo(Notification.Type.PERSISTENT)
        assertThat(notification.actions).containsExactly(
            NotificationAction.OpenSettings(message("actions.configure-manually"))
        )
    }

    @Test
    fun `can create platform auto detected notification`() {
        val notification = Notification.platformAutoDetected(GitHub())

        assertThat(notification.title).isNull()
        assertThat(notification.message).contains(GitHub().name)
        assertThat(notification.type).isEqualTo(Notification.Type.PERSISTENT)
        assertThat(notification.actions).containsExactly(
            NotificationAction.OpenSettings(message("notifications.platform-detected.action"))
        )
    }

    @Test
    fun `can create link copied notification`() {
        val link = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val notification = Notification.linkCopied(link)

        assertThat(notification.title).isEqualTo(message("name"))
        assertThat(notification.message).isEqualTo(message("notifications.copied-to-clipboard"))
        assertThat(notification.type).isEqualTo(Notification.Type.TRANSIENT)
        assertThat(notification.actions).containsExactly(NotificationAction.OpenUrl(link))
    }
}
