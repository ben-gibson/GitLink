package uk.co.ben_gibson.git.link.git

import git4idea.repo.GitRemote
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class RemoteTest {

    companion object {

        @JvmStatic
        fun httpUrlExpectationsProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "git@github.com:ben-gibson/GitLink.git",
                "http://github.com/ben-gibson/GitLink"
            ),
            Arguments.of(
                "https://username:password@github.com/ben-gibson/GitLink.git",
                "https://github.com/ben-gibson/GitLink"
            ),
            Arguments.of(
                "git@github.com:500/test.git",
                "http://github.com/500/test"
            ),
            Arguments.of(
                "https://foo@bitbucket.org/foo/bar",
                "https://bitbucket.org/foo/bar"
            ),
            Arguments.of(
                "ssh://git@stash.example.com:7999/foo/bar.git",
                "http://stash.example.com/foo/bar"
            ),
            Arguments.of(
                "git://github.com/foo/bar",
                "http://github.com/foo/bar"
            ),
            Arguments.of(
                "ssh://git@custom.gitlab.url:10022/group/project.git",
                "http://custom.gitlab.url/group/project"
            ),
            Arguments.of(
                "git@ssh.dev.azure.com:v3/ben-gibson/test/test",
                "http://dev.azure.com/ben-gibson/test/test"
            ),
            Arguments.of(
                "http://ben-gibson@dev.azure.com/ben-gibson/test/_git/test",
                "http://dev.azure.com/ben-gibson/test/_git/test"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("httpUrlExpectationsProvider")
    fun canGetHttpUrl(gitUrl: String, expectedUrl: String) {
        val remote = GitRemote("origin", listOf(gitUrl), listOf(), listOf(), listOf())

        assertEquals(expectedUrl, remote.httpUrl.toString())
    }
}