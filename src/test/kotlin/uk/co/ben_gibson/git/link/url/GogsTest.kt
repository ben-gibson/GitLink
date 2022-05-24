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
import java.net.URI

class GogsTest {

    companion object {

        private val REMOTE_BASE_URL = URI("https://try.gogs.io/foo/bar")
        private const val BRANCH = "master"
        private val COMMIT = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        private val FILE = File("Foo.java", false, "src", false)
        private val LINE_SELECTION = LineSelection(10, 20)

        @JvmStatic
        fun urlExpectationsProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL, FILE, BRANCH, LINE_SELECTION),
                "https://try.gogs.io/foo/bar/src/master/src/Foo.java#L10-L20"
            ),
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL, FILE, BRANCH),
                "https://try.gogs.io/foo/bar/src/master/src/Foo.java"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL, FILE, COMMIT, LINE_SELECTION),
                "https://try.gogs.io/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java#L10-L20"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(
                    REMOTE_BASE_URL,
                    File("resources", true, "src/foo", false),
                    COMMIT
                ),
                "https://try.gogs.io/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/foo/resources"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(
                    REMOTE_BASE_URL,
                    File("my-project", true, "", true),
                    COMMIT
                ),
                "https://try.gogs.io/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c"),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL, FILE, COMMIT),
                "https://try.gogs.io/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java"
            ),
            Arguments.of(
                UrlOptionsCommit(REMOTE_BASE_URL, COMMIT),
                "https://try.gogs.io/foo/bar/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("urlExpectationsProvider")
    fun canGenerateUrl(options: UrlOptions, expectedUrl: String) {
        val factory = TemplatedUrlFactory(UrlTemplates.gogs())
        val url = factory.createUrl(options)

        assertEquals(expectedUrl, url.toString())
    }
}
