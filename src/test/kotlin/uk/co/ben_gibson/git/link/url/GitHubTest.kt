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

class GitHubTest {

    companion object {

        private val REMOTE_BASE_URL = URL.fromString("https://github.com/my/repo")
        private const val BRANCH = "master"
        private val COMMIT = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        private val FILE = File("Foo.java", false, "src", false)
        private val LINE_SELECTION = LineSelection(10, 20)

        @JvmStatic
        fun urlExpectationsProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE, BRANCH, LINE_SELECTION),
                "https://github.com/my/repo/blob/master/src/Foo.java#L10-L20"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE, BRANCH, LINE_SELECTION),
                "https://github.com/my/repo/blob/master/src/Foo.java#L10-L20"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(
                    File("my-image.png", false, "src/foo bar baz/images", false),
                    BRANCH
                ),
                "https://github.com/my/repo/blob/master/src/foo%20bar%20baz/images/my-image.png"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE, "main", COMMIT, LineSelection(10, 20)),
                "https://github.com/my/repo/blob/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java#L10-L20"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(
                    File("resources", true, "src/foo", false),
                    "main",
                    COMMIT
                ),
                "https://github.com/my/repo/tree/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/foo/resources"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(
                    File("my-project", true, "", true),
                    "main",
                    COMMIT
                ),
                "https://github.com/my/repo/tree/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE, "main", COMMIT),
                "https://github.com/my/repo/blob/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsCommit(COMMIT, "main"),
                "https://github.com/my/repo/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("urlExpectationsProvider")
    fun canGenerateUrl(baseUrl: URL, options: UrlOptions, expectedUrl: String) {
        val factory = TemplatedUrlFactory(UrlTemplates.gitHub())

        val url = factory.createUrl(baseUrl, options)

        assertEquals(expectedUrl, url.toString())
    }
}
