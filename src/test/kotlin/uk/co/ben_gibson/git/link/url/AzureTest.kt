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
        private val REMOTE_BASE_URL_WITH_COMPANY_AND_GIT = URL.fromString("https://dev.azure.com/company/project/_git/test")
        private val REMOTE_BASE_URL_WITH_COMPANY_WITHOUT_GIT = URL.fromString("https://dev.azure.com/company/project/test")
        private const val BRANCH = "master"
        private val COMMIT = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        private val FILE = File("Foo.java", false, "src", false)
        private val LINE_SELECTION = LineSelection(10, 20)

        @JvmStatic
        fun urlExpectationsProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                REMOTE_BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtBranch(FILE, BRANCH, LINE_SELECTION),
                "https://dev.azure.com/ben-gibson/_git/test.git?version=GBmaster&path=src%2FFoo.java&line=10&lineEnd=21&lineStartColumn=1&lineEndColumn=1"
            ),
            Arguments.of(
                REMOTE_BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtBranch(FILE, BRANCH),
                "https://dev.azure.com/ben-gibson/_git/test.git?version=GBmaster&path=src%2FFoo.java"
            ),
            Arguments.of(
                REMOTE_BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtCommit(FILE, "main", COMMIT, LINE_SELECTION),
                "https://dev.azure.com/ben-gibson/_git/test.git?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2FFoo.java&line=10&lineEnd=21&lineStartColumn=1&lineEndColumn=1"
            ),
            Arguments.of(
                REMOTE_BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtCommit(
                    File("resources", true, "src/foo", false),
                    "main",
                    COMMIT
                ),
                "https://dev.azure.com/ben-gibson/_git/test.git?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2Ffoo%2Fresources"
            ),
            Arguments.of(
                REMOTE_BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtCommit(
                    File("my-project", true, "", true),
                    "main",
                    COMMIT
                ),
                "https://dev.azure.com/ben-gibson/_git/test.git?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=%2F"),
            Arguments.of(
                REMOTE_BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtCommit(FILE, "main", COMMIT),
                "https://dev.azure.com/ben-gibson/_git/test.git?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2FFoo.java"
            ),
            Arguments.of(
                REMOTE_BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsCommit(COMMIT, "main"),
                "https://dev.azure.com/ben-gibson/_git/test.git/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                REMOTE_BASE_URL_WITHOUT_GIT,
                UrlOptions.UrlOptionsCommit(COMMIT, "main"),
                "https://dev.azure.com/ben-gibson/_git/test.git/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                REMOTE_BASE_URL_WITH_COMPANY_AND_GIT,
                UrlOptions.UrlOptionsCommit(COMMIT, "main"),
                "https://dev.azure.com/company/project/_git/test.git/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                REMOTE_BASE_URL_WITH_COMPANY_WITHOUT_GIT,
                UrlOptions.UrlOptionsCommit(COMMIT, "main"),
                "https://dev.azure.com/company/project/_git/test.git/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
            Arguments.of(
                URL.fromString("https://ssh.dev.azure.com/v3/ben-gibson/test/test"),
                UrlOptions.UrlOptionsCommit(COMMIT, "main"),
                "https://dev.azure.com/ben-gibson/test/_git/test.git/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("urlExpectationsProvider")
    fun canGenerateUrl(baseUrl: URL, options: UrlOptions, expectedUrl: String) {
        val factory = AzureUrlFactory()
        val url = factory.createUrl(baseUrl, options)

        assertEquals(expectedUrl, url.toString())
    }
}
