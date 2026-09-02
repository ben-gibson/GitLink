package uk.co.ben_gibson.git.link.url

import org.junit.jupiter.api.TestFactory
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.git.LineSelection
import uk.co.ben_gibson.git.link.platform.Srht
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_MAIN
import uk.co.ben_gibson.git.link.url.UrlTestData.Expectation
import uk.co.ben_gibson.git.link.url.UrlTestData.assertUrls
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import uk.co.ben_gibson.url.URL

class SourceHutTest {
    private val baseUrl = URL.fromString("https://git.sr.ht/~myuser/myproject")
    private val commitSrht = Commit("23471005d2d874bb7ab400d45a2360f988c0be33")
    private val fileRust = File("main.rs", false, "src", false)
    private val lineSelectionSmall = LineSelection(1, 2)

    @TestFactory
    fun `should generate correct URLs`() = assertUrls(
        TemplatedUrlFactory(Srht().templates),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(fileRust, BRANCH_MAIN, lineSelectionSmall),
            "https://git.sr.ht/~myuser/myproject/tree/main/item/src/main.rs#L1",
            "File at branch with line selection (SourceHut only uses start line)"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(fileRust, BRANCH_MAIN, null),
            "https://git.sr.ht/~myuser/myproject/tree/main/item/src/main.rs",
            "File at branch without line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(fileRust, "main", commitSrht, lineSelectionSmall),
            "https://git.sr.ht/~myuser/myproject/tree/23471005d2d874bb7ab400d45a2360f988c0be33/item/src/main.rs#L1",
            "File at commit with line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(fileRust, "main", commitSrht, null),
            "https://git.sr.ht/~myuser/myproject/tree/23471005d2d874bb7ab400d45a2360f988c0be33/item/src/main.rs",
            "File at commit without line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.Commit(commitSrht, "main"),
            "https://git.sr.ht/~myuser/myproject/tree/23471005d2d874bb7ab400d45a2360f988c0be33",
            "Direct commit URL"
        ),
    )
}
