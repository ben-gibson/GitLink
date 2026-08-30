package uk.co.ben_gibson.git.link.platform

import com.intellij.openapi.components.service
import com.intellij.testFramework.junit5.TestApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.url.Host
import java.util.UUID
import java.util.stream.Stream

@TestApplication
class PlatformRepositoryTest {

    companion object {
        private val PLATFORM_DOMAINS = listOf(
            "github.com" to GitHub(),
            "gitlab.com" to GitLab(),
            "bitbucket.org" to BitbucketCloud(),
            "bitbucket.example.com" to BitbucketServer(),
            "gitee.com" to Gitee(),
            "gitea.com" to Gitea(),
            "gitea.example.com" to Gitea(),
            "gogs.io" to Gogs(),
            "gogs.example.com" to Gogs(),
            "git.sr.ht" to Srht(),
            "dev.azure.com" to Azure(),
            "azure.example.com" to Azure(),
            "googlesource.com" to Chromium(),
            "gerrit.example.com" to Gerrit(),
            "codeberg.org" to Codeberg()
        )

        @JvmStatic
        fun platformDomains(): Stream<Arguments> = PLATFORM_DOMAINS
            .map { (domain, platform) -> Arguments.of(domain, platform) }
            .stream()
    }

    private val settings get() = service<ApplicationSettings>()
    private val subject = PlatformRepository()

    private val customPlatform = ApplicationSettings.CustomPlatformSettings(
        id = "0d0a2f7e-1c9a-4e46-9b4e-4f6a12c4a111",
        displayName = "My Platform",
        baseUrl = "git.example.com"
    )

    @BeforeEach
    fun reset() {
        settings.customPlatforms = listOf()
        settings.registeredDomains = mapOf()
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("platformDomains")
    fun `should match the domains a platform is served from`(domain: String, expected: Platform) {
        assertThat(subject.getByDomain(Host(domain))).isEqualTo(expected)
    }

    @Test
    fun `should expect a domain for every platform`() {
        assertThat(PLATFORM_DOMAINS.map { it.second }).containsAll(subject.getAll())
    }

    // Two platforms claim bitbucket.org: Bitbucket Cloud owns it outright, while Bitbucket Server takes any
    // host containing 'bitbucket' to cover self hosted installs. The exact match has to win, or links to
    // Bitbucket Cloud would be built from the Bitbucket Server templates.
    @Test
    fun `should prefer an exact match over a wildcard`() {
        assertThat(subject.getByDomain(Host("bitbucket.org"))).isEqualTo(BitbucketCloud())
    }

    @Test
    fun `should match a domain owned by a custom platform`() {
        settings.customPlatforms = listOf(customPlatform)

        assertThat(subject.getByDomain(Host("git.example.com")))
            .isEqualTo(subject.getById(customPlatform.id))
    }

    // See https://github.com/ben-gibson/GitLink/issues/369, where a self-hosted Gerrit was picked up by the
    // built-in wildcard instead of the custom platform the user had registered it against.
    @Test
    fun `should prefer a user registered domain over a wildcard`() {
        settings.customPlatforms = listOf(customPlatform)
        settings.registeredDomains = mapOf(customPlatform.id to setOf("gerrit.example.com"))

        assertThat(subject.getByDomain(Host("gerrit.example.com")))
            .isEqualTo(subject.getById(customPlatform.id))
    }

    @Test
    fun `should prefer a user registered domain over an exact match`() {
        settings.registeredDomains = mapOf(GitLab().id.toString() to setOf("github.com"))

        assertThat(subject.getByDomain(Host("github.com"))).isEqualTo(GitLab())
    }

    @Test
    fun `should not match an unknown domain`() {
        assertThat(subject.getByDomain(Host("git.unknown.com"))).isNull()
    }

    @Test
    fun `should ignore a user registered domain for a platform that no longer exists`() {
        settings.registeredDomains = mapOf(UUID.randomUUID().toString() to setOf("github.com"))

        assertThat(subject.getByDomain(Host("github.com"))).isEqualTo(GitHub())
    }
}
