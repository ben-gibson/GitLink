package uk.co.ben_gibson.git.link.url

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.git.*
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.factory.AzureUrlFactory
import java.util.stream.Stream
import uk.co.ben_gibson.url.URL

class AzureTest {

    companion object {

        private val REMOTE_BASE_URL_WITH_GIT = URL.fromString("https://dev.azure.com/ben-gibson/_git/test")
        private val REMOTE_BASE_URL_WITHOUT_GIT = URL.fromString("https://dev.azure.com/ben-gibson/test")
        private const val BRANCH = "master"
        private val COMMIT = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        private val FILE = File("Foo.java", false, "src", false)
        private val LINE_SELECTION = LineSelection(10, 20)

        @JvmStatic
        fun urlExpectationsProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL_WITH_GIT, FILE, BRANCH, LINE_SELECTION),
                "https://dev.azure.com/ben-gibson/_git/test?version=GBmaster&path=src%2FFoo.java&line=10&lineEnd=21&lineStartColumn=1&lineEndColumn=1"
            ),
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL_WITH_GIT, FILE, BRANCH),
                "https://dev.azure.com/ben-gibson/_git/test?version=GBmaster&path=src%2FFoo.java"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL_WITH_GIT, FILE, COMMIT, LINE_SELECTION),
                "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2FFoo.java&line=10&lineEnd=21&lineStartColumn=1&lineEndColumn=1"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(
                    REMOTE_BASE_URL_WITH_GIT,
                    File("resources", true, "src/foo", false),
                    COMMIT
                ),
                "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2Ffoo%2Fresources"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(
                    REMOTE_BASE_URL_WITH_GIT,
                    File("my-project", true, "", true),
                    COMMIT
                ),
                "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=%2F"),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL_WITH_GIT, FILE, COMMIT),
                "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2FFoo.java"
            ),
            Arguments.of(
                UrlOptionsCommit(REMOTE_BASE_URL_WITH_GIT, COMMIT),
                "https://dev.azure.com/ben-gibson/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                UrlOptionsCommit(REMOTE_BASE_URL_WITHOUT_GIT, COMMIT),
                "https://dev.azure.com/ben-gibson/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("urlExpectationsProvider")
    fun canGenerateUrl(options: UrlOptions, expectedUrl: String) {
        val factory = AzureUrlFactory()
        val url = factory.createUrl(options)

        assertEquals(expectedUrl, url.toString())
    }
}
