package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.components.service
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.url.URL

@TestApplication
class ForceHttpsTest {

    private val projectFixture = projectFixture()
    private val project get() = projectFixture.get()
    private val subject = ForceHttps()

    @Test
    fun `should convert the URL to HTTPS when forced`() {
        // Given
        project.service<ProjectSettings>().forceHttps = true
        val httpUrl = URL.fromString("http://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { httpUrl }

        // Then
        assertThat(result.toString()).startsWith("https://")
    }

    @Test
    fun `should leave the URL unchanged when not forced`() {
        // Given
        project.service<ProjectSettings>().forceHttps = false
        val httpUrl = URL.fromString("http://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { httpUrl }

        // Then
        assertThat(result.toString()).startsWith("http://")
    }

    @Test
    fun `should leave an HTTPS URL unchanged`() {
        // Given
        project.service<ProjectSettings>().forceHttps = true
        val httpsUrl = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { httpsUrl }

        // Then
        assertThat(result.toString()).startsWith("https://")
    }

    @Test
    fun `should pass null through`() {
        // Given
        project.service<ProjectSettings>().forceHttps = true
        val pass = pass(project)

        // When
        val result = subject(pass) { null }

        // Then
        assertThat(result).isNull()
    }
}
