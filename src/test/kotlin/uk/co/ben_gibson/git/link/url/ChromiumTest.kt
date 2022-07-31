package uk.co.ben_gibson.git.link.url

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import java.util.stream.Stream
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.url.factory.ChromiumUrlFactory
import uk.co.ben_gibson.url.URL

class ChromiumTest {

    companion object {

        private val REMOTE_BASE_URL_CHROMIUMOS = URL.fromString("https://chromium.googlesource.com/chromiumos/platform/ec")
        private val REMOTE_BASE_URL_CHROMIUM = URL.fromString("https://chromium.googlesource.com/chromium/tools/build")
        private const val BRANCH = "master"
        private val COMMIT = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        private val FILE = File("foo.c", false, "board", false)
        private val LINE_SELECTION = LineSelection(10, 20)

        @JvmStatic
        fun urlExpectationsProvider(): Stream<Arguments> = Stream.of(
            // Chromiumos file at branch
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL_CHROMIUMOS, FILE, BRANCH, LINE_SELECTION),
                "https://source.chromium.org/chromiumos/chromiumos/codesearch/+/master:src/platform/ec/board/foo.c;l=10-20"
            ),
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL_CHROMIUMOS, FILE, BRANCH),
                "https://source.chromium.org/chromiumos/chromiumos/codesearch/+/master:src/platform/ec/board/foo.c"
            ),

            // Chromium file at branch
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL_CHROMIUM, FILE, BRANCH),
                "https://source.chromium.org/chromium/chromium/tools/build/+/master:board/foo.c"
            ),
            Arguments.of(
                UrlOptionsFileAtBranch(REMOTE_BASE_URL_CHROMIUM, FILE, BRANCH, LINE_SELECTION),
                "https://source.chromium.org/chromium/chromium/tools/build/+/master:board/foo.c;l=10-20"
            ),

            // Chromiumos file at commit
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL_CHROMIUMOS, FILE, COMMIT, LINE_SELECTION),
                "https://source.chromium.org/chromiumos/chromiumos/codesearch/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c:src/platform/ec/board/foo.c;l=10-20"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL_CHROMIUMOS, FILE, COMMIT),
                "https://source.chromium.org/chromiumos/chromiumos/codesearch/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c:src/platform/ec/board/foo.c"
            ),

            // Chromium file at commit
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL_CHROMIUM, FILE, COMMIT, LINE_SELECTION),
                "https://source.chromium.org/chromium/chromium/tools/build/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c:board/foo.c;l=10-20"
            ),
            Arguments.of(
                UrlOptionsFileAtCommit(REMOTE_BASE_URL_CHROMIUM, FILE, COMMIT),
                "https://source.chromium.org/chromium/chromium/tools/build/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c:board/foo.c"
            ),

            // Chromiumos commit
            Arguments.of(
                UrlOptionsCommit(REMOTE_BASE_URL_CHROMIUMOS, COMMIT),
                "https://source.chromium.org/chromiumos/_/chromium/chromiumos/platform/ec/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),

            // Chromium commit
            Arguments.of(
                UrlOptionsCommit(REMOTE_BASE_URL_CHROMIUM, COMMIT),
                "https://source.chromium.org/chromium/chromium/tools/build/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c"
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("urlExpectationsProvider")
    fun canGenerateUrl(options: UrlOptions, expectedUrl: String) {
        val factory = ChromiumUrlFactory()
        val url = factory.createUrl(options)

        assertEquals(expectedUrl, url.toString())
    }
}
