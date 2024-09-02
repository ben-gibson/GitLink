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

class SourceHutTest {

    companion object {

        private val REMOTE_BASE_URL = URL.fromString("https://git.sr.ht/~myuser/myproject")
        private const val BRANCH = "main"
        private val COMMIT = Commit("23471005d2d874bb7ab400d45a2360f988c0be33")
        private val FILE = File("main.rs", false, "src", false)
        private val LINE_SELECTION = LineSelection(1, 2)

        @JvmStatic
        fun urlExpectationsProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE, BRANCH, LINE_SELECTION),
                "https://git.sr.ht/~myuser/myproject/tree/main/item/src/main.rs#L1"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE, BRANCH),
                "https://git.sr.ht/~myuser/myproject/tree/main/item/src/main.rs"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE, "main", COMMIT, LINE_SELECTION),
                "https://git.sr.ht/~myuser/myproject/tree/23471005d2d874bb7ab400d45a2360f988c0be33/item/src/main.rs#L1"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE, "main", COMMIT),
                "https://git.sr.ht/~myuser/myproject/tree/23471005d2d874bb7ab400d45a2360f988c0be33/item/src/main.rs"
            ),
            Arguments.of(
                REMOTE_BASE_URL,
                UrlOptions.UrlOptionsCommit(COMMIT, "main"),
                "https://git.sr.ht/~myuser/myproject/tree/23471005d2d874bb7ab400d45a2360f988c0be33"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("urlExpectationsProvider")
    fun canGenerateUrl(baseUrl: URL, options: UrlOptions, expectedUrl: String) {
        val factory = TemplatedUrlFactory(UrlTemplates.srht())
        val url = factory.createUrl(baseUrl, options)

        assertEquals(expectedUrl, url.toString())
    }
}