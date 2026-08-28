package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.components.service
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.url.URL

@TestApplication
class SendSupportNotificationTest {

    private val projectFixture = projectFixture()
    private val project get() = projectFixture.get()
    private val subject = SendSupportNotification()

    @Test
    fun `should pass the URL through unchanged`() {
        // Given
        service<ApplicationSettings>().apply { requestSupport = false; hits = 0 }
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { url }

        // Then
        assertThat(result).isEqualTo(url)
    }

    @Test
    fun `should pass null through`() {
        // Given
        service<ApplicationSettings>().apply { requestSupport = false; hits = 0 }
        val pass = pass(project)

        // When
        val result = subject(pass) { null }

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should pass the URL through when support requests are disabled`() {
        // Given
        service<ApplicationSettings>().apply { requestSupport = false; hits = 5 }
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { url }

        // Then
        assertThat(result).isEqualTo(url)
    }

    @Test
    fun `should pass the URL through when hits are below the threshold`() {
        // Given
        service<ApplicationSettings>().apply { requestSupport = true; hits = 3 }
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { url }

        // Then
        assertThat(result).isEqualTo(url)
    }

    @Test
    fun `should pass the URL through on the fifth hit`() {
        // Given
        service<ApplicationSettings>().apply { requestSupport = true; hits = 5 }
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { url }

        // Then
        assertThat(result).isEqualTo(url)
    }

    @Test
    fun `should pass the URL through on every fiftieth hit`() {
        // Given
        service<ApplicationSettings>().apply { requestSupport = true; hits = 100 }
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { url }

        // Then
        assertThat(result).isEqualTo(url)
    }
}
