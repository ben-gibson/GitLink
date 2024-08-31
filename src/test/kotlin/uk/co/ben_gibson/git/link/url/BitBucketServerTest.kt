package uk.co.ben_gibson.git.link.url

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.git.*
import uk.co.ben_gibson.git.link.ui.LineSelection
import java.util.stream.Stream
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import uk.co.ben_gibson.url.URL

class BitBucketServerTest {

    companion object {

        private val REMOTE_BASE_URL = URL.fromString("https://stash.example.com/foo/bar")
        private const val BRANCH = "master"
        private val COMMIT = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        private val FILE = File("Foo.java", false, "src", false)
        private val LINE_SELECTION = LineSelection(10, 20)

        @JvmStatic
        fun urlExpectationsProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE, BRANCH, LINE_SELECTION),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/master#10-20"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE, BRANCH),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/master"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE, COMMIT, LineSelection(10, 20)),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c#10-20"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(
                    File("resources", true, "src/foo", false),
                    COMMIT
                ),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/foo/resources?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(
                    File("my-project", true, "", true),
                    COMMIT
                ),
                "https://stash.example.com/projects/foo/repos/bar/browse?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE, COMMIT),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsCommit(COMMIT),
                "https://stash.example.com/projects/foo/repos/bar/commits/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("urlExpectationsProvider")
    fun canGenerateUrl(baseUrl: URL, options: UrlOptions, expectedUrl: String) {
        val factory = TemplatedUrlFactory(UrlTemplates.bitbucketServer())
        val url = factory.createUrl(baseUrl, options)

        assertEquals(expectedUrl, url.toString())
    }
}
