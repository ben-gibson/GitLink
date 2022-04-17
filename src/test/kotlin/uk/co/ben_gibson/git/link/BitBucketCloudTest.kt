package uk.co.ben_gibson.git.link

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.git.BITBUCKET_CLOUD
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.UrlOptions
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtBranch
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import java.net.URL
import java.util.stream.Stream
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.url.UrlOptionsCommit
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtCommit

class BitBucketCloudTest {

    companion object {

        private val REMOTE_BASE_URL = URL("https://bitbucket.org/foo/bar")
        private const val BRANCH = "master"
        private val COMMIT = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        private val FILE = File("Foo.java", false, "src", false)
        private val LINE_SELECTION = LineSelection(10, 20)

        @JvmStatic
        fun urlExpectationsProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL, FILE, BRANCH, LINE_SELECTION),
                "https://bitbucket.org/foo/bar/src/HEAD/src/Foo.java?at=master#lines-10:20"
            ),
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL, FILE, BRANCH),
                "https://bitbucket.org/foo/bar/src/HEAD/src/Foo.java?at=master"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL, FILE, COMMIT, LineSelection(10, 20)),
                "https://bitbucket.org/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java#lines-10:20"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(
                    REMOTE_BASE_URL,
                    File("resources", true, "src/foo", false),
                    COMMIT
                ),
                "https://bitbucket.org/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/foo/resources"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(
                    REMOTE_BASE_URL,
                    File("my-project", true, "", true),
                    COMMIT
                ),
                "https://bitbucket.org/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL, FILE, COMMIT),
                "https://bitbucket.org/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java"
            ),
            Arguments.of(
                UrlOptionsCommit(REMOTE_BASE_URL, COMMIT),
                "https://bitbucket.org/foo/bar/commits/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("urlExpectationsProvider")
    fun canGenerateUrl(options: UrlOptions, expectedUrl: String) {
        val url = TemplatedUrlFactory(BITBUCKET_CLOUD.urlTemplate).createUrl(options)

        assertEquals(expectedUrl, url.toString())
    }
}
