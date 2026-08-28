package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.disposableFixture
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.intellij.testFramework.replaceService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.Notifier
import uk.co.ben_gibson.git.link.ui.notification.TestNotifier
import uk.co.ben_gibson.url.URL

@TestApplication
class TimerTest {

    private val projectFixture = projectFixture()
    private val disposableFixture = disposableFixture()
    private val project get() = projectFixture.get()
    private lateinit var notifier: TestNotifier

    private val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")

    @BeforeEach
    fun setUp() {
        notifier = TestNotifier()

        ApplicationManager.getApplication().replaceService(
            Notifier::class.java,
            notifier,
            disposableFixture.get()
        )
    }

    @Test
    fun `should pass the URL through when remote checks are disabled`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = false; showPerformanceTip = true }
        val subject = Timer(FixedInstantSource())

        // When
        val result = subject(pass(project)) { url }

        // Then
        assertThat(result).isEqualTo(url)
        assertThat(notifier.notifications).isEmpty()
    }

    @Test
    fun `should pass the URL through when the performance tip is disabled`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = true; showPerformanceTip = false }
        val subject = Timer(FixedInstantSource())

        // When
        val result = subject(pass(project)) { url }

        // Then
        assertThat(result).isEqualTo(url)
        assertThat(notifier.notifications).isEmpty()
    }

    @Test
    fun `should pass null through when timing is disabled`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = false; showPerformanceTip = false }
        val subject = Timer(FixedInstantSource())

        // When
        val result = subject(pass(project)) { null }

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should pass null through when timing is enabled`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = true; showPerformanceTip = true }
        val subject = Timer(FixedInstantSource(0, 10))

        // When
        val result = subject(pass(project)) { null }

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should not warn when generation is fast`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = true; showPerformanceTip = true }
        val subject = Timer(FixedInstantSource(0, 1_000))

        // When
        val result = subject(pass(project)) { url }

        // Then
        assertThat(result).isEqualTo(url)
        assertThat(notifier.notifications).isEmpty()
    }

    @Test
    fun `should warn when generation takes over a second`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = true; showPerformanceTip = true }
        val subject = Timer(FixedInstantSource(0, 1_001))

        // When
        val result = subject(pass(project)) { url }

        // Then
        assertThat(result).isEqualTo(url)
        assertThat(notifier.notifications).containsExactly(Notification.performanceTips())
    }
}
