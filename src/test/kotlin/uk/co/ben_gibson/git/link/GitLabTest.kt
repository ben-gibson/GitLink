package uk.co.ben_gibson.git.link

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.UrlOptions
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtBranch
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import java.net.URL
import java.util.stream.Stream
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.GIT_LAB
import uk.co.ben_gibson.git.link.url.UrlOptionsCommit
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtCommit

class GitLabTest {

    companion object {

        private val REMOTE_BASE_URL = URL("https://gitlab.com/my/repo/")
        private const val BRANCH = "master"
        private val COMMIT = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        private val FILE = File("Foo.java", false, "src", false)
        private val LINE_SELECTION = LineSelection(10, 20)

        @JvmStatic
        fun urlExpectationsProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL, FILE, BRANCH, LINE_SELECTION),
                "https://gitlab.com/my/repo/blob/master/src/Foo.java#L10-20"
            ),
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL, FILE, BRANCH),
                "https://gitlab.com/my/repo/blob/master/src/Foo.java"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL, FILE, COMMIT, LineSelection(10, 20)),
                "https://gitlab.com/my/repo/blob/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java#L10-20"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(
                    REMOTE_BASE_URL,
                    File("resources", true, "src/foo", false),
                    COMMIT
                ),
                "https://gitlab.com/my/repo/tree/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/foo/resources"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(
                    REMOTE_BASE_URL,
                    File("my-project", true, "", true),
                    COMMIT
                ),
                "https://gitlab.com/my/repo/tree/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL, FILE, COMMIT),
                "https://gitlab.com/my/repo/blob/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java"
            ),
            Arguments.of(
                UrlOptionsCommit(REMOTE_BASE_URL, COMMIT),
                "https://gitlab.com/my/repo/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("urlExpectationsProvider")
    fun canGenerateUrl(options: UrlOptions, expectedUrl: String) {
        val url = TemplatedUrlFactory(GIT_LAB.urlTemplate).createUrl(options)

        assertEquals(expectedUrl, url.toString())
    }
}
