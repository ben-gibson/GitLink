package uk.co.ben_gibson.git.link.platform

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import uk.co.ben_gibson.url.Host

class DomainTest {
    @ParameterizedTest(name = "{0} matches {1} -> {2}")
    @CsvSource(
        "github.com, github.com, true",
        "github.com, gitlab.com, false",
        "github.com, GITHUB.COM, true",
        "github.com, my-github.com, false",
        // A dot is a regex metacharacter, so an unescaped domain would let any character match it.
        "github.com, githubXcom, false"
    )
    fun `should match a host`(domain: String, host: String, expected: Boolean) {
        assertThat(Domain.of(domain).matches(Host(host))).isEqualTo(expected)
    }

    @ParameterizedTest(name = "wildcard {0} matches {1} -> {2}")
    @CsvSource(
        "gerrit, gerrit.wikimedia.org, true",
        "gerrit, GERRIT.example.com, true",
        "gerrit, review.gerrit.example.com, true",
        "gerrit, github.com, false",
        "bitbucket, bitbucket.example.com, true",
        "bitbucket, bitbucket.org, true",
        "azure, dev.azure.com, true",
        "azure, vs-ssh.visualstudio.com, false"
    )
    fun `should match a host by wildcard`(fragment: String, host: String, expected: Boolean) {
        assertThat(Domain.wildcard(fragment).matches(Host(host))).isEqualTo(expected)
    }

    @Test
    fun `should describe itself`() {
        assertThat(Domain.of("github.com")).hasToString("github.com")
        assertThat(Domain.wildcard("gerrit")).hasToString("*gerrit*")
    }

    @Test
    fun `should only flag a wildcard`() {
        assertThat(Domain.of("github.com").isWildcard).isFalse()
        assertThat(Domain.wildcard("gerrit").isWildcard).isTrue()
    }

    @Test
    fun `should not equal the same text as a wildcard`() {
        assertThat(Domain.of("gerrit")).isNotEqualTo(Domain.wildcard("gerrit"))
        assertThat(Domain.of("github.com")).isEqualTo(Domain.of("GITHUB.COM"))
    }
}
