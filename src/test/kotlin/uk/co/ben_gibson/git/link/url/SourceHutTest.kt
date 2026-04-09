package uk.co.ben_gibson.git.link.url

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_MAIN
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import uk.co.ben_gibson.url.URL
import java.util.stream.Stream

class SourceHutTest {

    companion object {
        private val BASE_URL = URL.fromString("https://git.sr.ht/~myuser/myproject")
        private val COMMIT_SRHT = Commit("23471005d2d874bb7ab400d45a2360f988c0be33")
        private val FILE_RUST = File("main.rs", false, "src", false)
        private val LINE_SELECTION_SMALL = LineSelection(1, 2)

        @JvmStatic
        fun urlExpectations(): Stream<Arguments> = Stream.of(
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE_RUST, BRANCH_MAIN, LINE_SELECTION_SMALL),
                "https://git.sr.ht/~myuser/myproject/tree/main/item/src/main.rs#L1",
                "File at branch with line selection (SourceHut only uses start line)"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE_RUST, BRANCH_MAIN, null),
                "https://git.sr.ht/~myuser/myproject/tree/main/item/src/main.rs",
                "File at branch without line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE_RUST, "main", COMMIT_SRHT, LINE_SELECTION_SMALL),
                "https://git.sr.ht/~myuser/myproject/tree/23471005d2d874bb7ab400d45a2360f988c0be33/item/src/main.rs#L1",
                "File at commit with line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE_RUST, "main", COMMIT_SRHT, null),
                "https://git.sr.ht/~myuser/myproject/tree/23471005d2d874bb7ab400d45a2360f988c0be33/item/src/main.rs",
                "File at commit without line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsCommit(COMMIT_SRHT, "main"),
                "https://git.sr.ht/~myuser/myproject/tree/23471005d2d874bb7ab400d45a2360f988c0be33",
                "Direct commit URL"
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
        val factory = TemplatedUrlFactory(UrlTemplates.srht())

        // When
        val url = factory.createUrl(baseUrl, options)

        // Then
        assertThat(url.toString())
            .describedAs(description)
            .isEqualTo(expectedUrl)
    }
}