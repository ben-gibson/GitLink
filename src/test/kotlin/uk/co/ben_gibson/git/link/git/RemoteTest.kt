package uk.co.ben_gibson.git.link.git

import git4idea.repo.GitRemote
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.url.Host
import java.util.stream.Stream

class RemoteTest {

    @Nested
    inner class `HTTP URL Conversion` {

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("uk.co.ben_gibson.git.link.git.RemoteTest#httpUrlExpectations")
        fun `should convert git URL to HTTP`(gitUrl: String, expectedUrl: String, description: String) {
            // Given
            val remote = GitRemote("origin", listOf(gitUrl), emptyList(), emptyList(), emptyList())

            // When
            val httpUrl = remote.httpUrl

            // Then
            assertThat(httpUrl.toString())
                .describedAs(description)
                .isEqualTo(expectedUrl)
        }

        @Test
        fun `should return null for empty remote`() {
            // Given
            val remote = GitRemote("origin", emptyList(), emptyList(), emptyList(), emptyList())

            // When
            val httpUrl = remote.httpUrl

            // Then
            assertThat(httpUrl).isNull()
        }

        @Test
        fun `should use first URL`() {
            // Given
            val remote = GitRemote(
                "origin",
                listOf(
                    "git@github.com:user/repo.git",
                    "https://github.com/user/repo.git"
                ),
                emptyList(),
                emptyList(),
                emptyList()
            )

            // When
            val httpUrl = remote.httpUrl

            // Then
            assertThat(httpUrl.toString()).isEqualTo("http://github.com/user/repo")
        }

        @ParameterizedTest
        @CsvSource(
            "git@github.com:user/repo.git, origin",
            "git@gitlab.com:user/repo.git, upstream",
            "git@bitbucket.org:user/repo.git, fork"
        )
        fun `should work with different remote names`(gitUrl: String, remoteName: String) {
            // Given
            val remote = GitRemote(remoteName, listOf(gitUrl), emptyList(), emptyList(), emptyList())

            // When
            val httpUrl = remote.httpUrl

            // Then
            assertThat(httpUrl).isNotNull()
        }
    }

    @Nested
    inner class `Domain Extraction` {

        @ParameterizedTest
        @CsvSource(
            "git@github.com:user/repo.git, github.com",
            "https://gitlab.com/user/repo.git, gitlab.com",
            "git@bitbucket.org:user/repo.git, bitbucket.org",
            "https://git.company.com/user/repo.git, git.company.com",
            "ssh://git@custom.host:7999/repo.git, custom.host"
        )
        fun `should extract domain`(gitUrl: String, expectedDomain: String) {
            // Given
            val remote = GitRemote("origin", listOf(gitUrl), emptyList(), emptyList(), emptyList())

            // When
            val domain = remote.domain

            // Then
            assertThat(domain).isEqualTo(Host(expectedDomain))
        }

        @Test
        fun `should return null domain for empty remote`() {
            // Given
            val remote = GitRemote("origin", emptyList(), emptyList(), emptyList(), emptyList())

            // When
            val domain = remote.domain

            // Then
            assertThat(domain).isNull()
        }
    }

    @Nested
    inner class `Edge Cases` {

        @Test
        fun `should handle URLs with whitespace`() {
            // Given
            val remote = GitRemote(
                "origin",
                listOf("  git@github.com:user/repo.git  "),
                emptyList(),
                emptyList(),
                emptyList()
            )

            // When
            val httpUrl = remote.httpUrl

            // Then
            assertThat(httpUrl.toString()).isEqualTo("http://github.com/user/repo")
        }

        @Test
        fun `should handle repository names with special characters`() {
            // Given
            val remote = GitRemote(
                "origin",
                listOf("git@github.com:user/repo-with-dashes.git"),
                emptyList(),
                emptyList(),
                emptyList()
            )

            // When
            val httpUrl = remote.httpUrl

            // Then
            assertThat(httpUrl.toString()).isEqualTo("http://github.com/user/repo-with-dashes")
        }

        @Test
        fun `should handle deeply nested repository paths`() {
            // Given
            val remote = GitRemote(
                "origin",
                listOf("git@gitlab.com:group/subgroup/subsubgroup/project.git"),
                emptyList(),
                emptyList(),
                emptyList()
            )

            // When
            val httpUrl = remote.httpUrl

            // Then
            assertThat(httpUrl.toString()).isEqualTo("http://gitlab.com/group/subgroup/subsubgroup/project")
        }
    }

    companion object {
        @JvmStatic
        fun httpUrlExpectations(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "git@github.com:ben-gibson/GitLink.git",
                "http://github.com/ben-gibson/GitLink",
                "SSH URL with SCP syntax"
            ),
            Arguments.of(
                "https://username:password@github.com/ben-gibson/GitLink.git",
                "https://github.com/ben-gibson/GitLink",
                "HTTPS URL with credentials should strip credentials"
            ),
            Arguments.of(
                "git@github.com:500/test.git",
                "http://github.com/500/test",
                "Repository name with only digits (regression test for #94)"
            ),
            Arguments.of(
                "https://foo@bitbucket.org/foo/bar",
                "https://bitbucket.org/foo/bar",
                "HTTPS URL with username only"
            ),
            Arguments.of(
                "ssh://git@stash.example.com:7999/foo/bar.git",
                "http://stash.example.com/foo/bar",
                "SSH URL with custom port should strip port"
            ),
            Arguments.of(
                "git://github.com/foo/bar",
                "http://github.com/foo/bar",
                "Git protocol URL"
            ),
            Arguments.of(
                "ssh://git@custom.gitlab.url:10022/group/project.git",
                "http://custom.gitlab.url/group/project",
                "Self-hosted GitLab with custom port"
            ),
            Arguments.of(
                "xy://custom.gitlab.url/group/project.git",
                "http://custom.gitlab.url/group/project",
                "Custom protocol should convert to HTTP"
            ),
            Arguments.of(
                "http://ben-gibson@dev.azure.com/ben-gibson/test/_git/test.git",
                "http://dev.azure.com/ben-gibson/test/_git/test.git",
                "Azure DevOps URL (preserves .git suffix)"
            ),
            Arguments.of(
                "rad://z37oHWbEomJXUAqxd9hoQHWkg2pC8",
                "http://z37oHWbEomJXUAqxd9hoQHWkg2pC8",
                "Radicle URL format"
            ),
            Arguments.of(
                "https://github.com/user/repo.git",
                "https://github.com/user/repo",
                "Standard HTTPS URL should strip .git"
            ),
            Arguments.of(
                "http://gitlab.com/user/repo.git",
                "http://gitlab.com/user/repo",
                "HTTP URL should strip .git"
            )
        )
    }
}
