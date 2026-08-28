package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.components.service
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.url.URL

@TestApplication
class TimerTest {

    private val projectFixture = projectFixture()
    private val project get() = projectFixture.get()
    private val subject = Timer()

    @Test
    fun `should pass the URL through when remote checks are disabled`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = false; showPerformanceTip = true }
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { url }

        // Then
        assertThat(result).isEqualTo(url)
    }

    @Test
    fun `should pass the URL through when the performance tip is disabled`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = true; showPerformanceTip = false }
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { url }

        // Then
        assertThat(result).isEqualTo(url)
    }

    @Test
    fun `should pass null through when timing is disabled`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = false; showPerformanceTip = false }
        val pass = pass(project)

        // When
        val result = subject(pass) { null }

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should pass the URL through when timing is enabled`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = true; showPerformanceTip = true }
        val url = URL.fromString("https://github.com/user/repo/blob/main/file.kt")
        val pass = pass(project)

        // When
        val result = subject(pass) { url }

        // Then
        assertThat(result).isEqualTo(url)
    }

    @Test
    fun `should pass null through when timing is enabled`() {
        // Given
        project.service<ProjectSettings>().apply { shouldCheckRemote = true; showPerformanceTip = true }
        val pass = pass(project)

        // When
        val result = subject(pass) { null }

        // Then
        assertThat(result).isNull()
    }
}
