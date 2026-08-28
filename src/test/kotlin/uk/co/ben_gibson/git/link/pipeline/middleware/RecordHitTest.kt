package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.components.service
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.url.URL

@TestApplication
class RecordHitTest {

    private val projectFixture = projectFixture()
    private val project get() = projectFixture.get()
    private val subject = RecordHit()

    @Test
    fun `should record a hit when a URL is generated`() {
        // Given
        service<ApplicationSettings>().hits = 0
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { url }

        // Then
        assertThat(result).isEqualTo(url)
        assertThat(service<ApplicationSettings>().hits).isEqualTo(1)
    }

    @Test
    fun `should not record a hit when no URL is generated`() {
        // Given
        service<ApplicationSettings>().hits = 0
        val pass = pass(project)

        // When
        val result = subject(pass) { null }

        // Then
        assertThat(result).isNull()
        assertThat(service<ApplicationSettings>().hits).isEqualTo(0)
    }

    @Test
    fun `should add to the existing hit count`() {
        // Given
        service<ApplicationSettings>().hits = 5
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        subject(pass) { url }

        // Then
        assertThat(service<ApplicationSettings>().hits).isEqualTo(6)
    }
}
