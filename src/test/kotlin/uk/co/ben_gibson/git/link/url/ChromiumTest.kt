package uk.co.ben_gibson.git.link.url

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_MASTER
import uk.co.ben_gibson.git.link.url.UrlTestData.COMMIT_FULL
import uk.co.ben_gibson.git.link.url.UrlTestData.LINE_SELECTION_RANGE
import uk.co.ben_gibson.git.link.url.factory.ChromiumUrlFactory
import uk.co.ben_gibson.url.URL
import java.util.stream.Stream

class ChromiumTest {

    companion object {
        private val BASE_URL_CHROMIUMOS = URL.fromString("https://chromium.googlesource.com/chromiumos/platform/ec")
        private val BASE_URL_CHROMIUM = URL.fromString("https://chromium.googlesource.com/chromium/tools/build")
        private val FILE_C = File("foo.c", false, "board", false)

        @JvmStatic
        fun urlExpectations(): Stream<Arguments> = Stream.of(
            Arguments.of(
                BASE_URL_CHROMIUMOS,
                UrlOptions.UrlOptionsFileAtBranch(FILE_C, BRANCH_MASTER, LINE_SELECTION_RANGE),
                "https://source.chromium.org/chromiumos/chromiumos/codesearch/+/master:src/platform/ec/board/foo.c;l=10-20",
                "ChromiumOS file at branch with line selection"
            ),
            Arguments.of(
                BASE_URL_CHROMIUMOS,
                UrlOptions.UrlOptionsFileAtBranch(FILE_C, BRANCH_MASTER, null),
                "https://source.chromium.org/chromiumos/chromiumos/codesearch/+/master:src/platform/ec/board/foo.c",
                "ChromiumOS file at branch without line selection"
            ),
            Arguments.of(
                BASE_URL_CHROMIUM,
                UrlOptions.UrlOptionsFileAtBranch(FILE_C, BRANCH_MASTER, null),
                "https://source.chromium.org/chromium/chromium/tools/build/+/master:board/foo.c",
                "Chromium file at branch without line selection"
            ),
            Arguments.of(
                BASE_URL_CHROMIUM,
                UrlOptions.UrlOptionsFileAtBranch(FILE_C, BRANCH_MASTER, LINE_SELECTION_RANGE),
                "https://source.chromium.org/chromium/chromium/tools/build/+/master:board/foo.c;l=10-20",
                "Chromium file at branch with line selection"
            ),
            Arguments.of(
                BASE_URL_CHROMIUMOS,
                UrlOptions.UrlOptionsFileAtCommit(FILE_C, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
                "https://source.chromium.org/chromiumos/chromiumos/codesearch/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c:src/platform/ec/board/foo.c;l=10-20",
                "ChromiumOS file at commit with line selection"
            ),
            Arguments.of(
                BASE_URL_CHROMIUMOS,
                UrlOptions.UrlOptionsFileAtCommit(FILE_C, "main", COMMIT_FULL, null),
                "https://source.chromium.org/chromiumos/chromiumos/codesearch/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c:src/platform/ec/board/foo.c",
                "ChromiumOS file at commit without line selection"
            ),
            Arguments.of(
                BASE_URL_CHROMIUM,
                UrlOptions.UrlOptionsFileAtCommit(FILE_C, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
                "https://source.chromium.org/chromium/chromium/tools/build/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c:board/foo.c;l=10-20",
                "Chromium file at commit with line selection"
            ),
            Arguments.of(
                BASE_URL_CHROMIUM,
                UrlOptions.UrlOptionsFileAtCommit(FILE_C, "main", COMMIT_FULL, null),
                "https://source.chromium.org/chromium/chromium/tools/build/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c:board/foo.c",
                "Chromium file at commit without line selection"
            ),
            Arguments.of(
                BASE_URL_CHROMIUMOS,
                UrlOptions.UrlOptionsCommit(COMMIT_FULL, "main"),
                "https://source.chromium.org/chromiumos/_/chromium/chromiumos/platform/ec/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "ChromiumOS direct commit URL"
            ),
            Arguments.of(
                BASE_URL_CHROMIUM,
                UrlOptions.UrlOptionsCommit(COMMIT_FULL, "main"),
                "https://source.chromium.org/chromium/chromium/tools/build/+/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Chromium direct commit URL"
            )
        )
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource("urlExpectations")
    fun `should generate correct URLs`(
        baseUrl: URL,
        options: UrlOptions,
        expectedUrl: String,
        description: String
    ) {
        // Given
        val factory = ChromiumUrlFactory()

        // When
        val url = factory.createUrl(baseUrl, options)

        // Then
        assertThat(url.toString())
            .describedAs(description)
            .isEqualTo(expectedUrl)
    }
}
