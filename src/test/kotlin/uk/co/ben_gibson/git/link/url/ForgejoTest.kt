package uk.co.ben_gibson.git.link.url

import org.junit.jupiter.api.TestFactory
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.platform.Forgejo
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_MASTER
import uk.co.ben_gibson.git.link.url.UrlTestData.COMMIT_FULL
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_RESOURCES
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_ROOT
import uk.co.ben_gibson.git.link.url.UrlTestData.Expectation
import uk.co.ben_gibson.git.link.url.UrlTestData.FILE_JAVA
import uk.co.ben_gibson.git.link.url.UrlTestData.LINE_SELECTION_RANGE
import uk.co.ben_gibson.git.link.url.UrlTestData.assertUrls
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import uk.co.ben_gibson.url.URL

class ForgejoTest {
    private val baseUrl = URL.fromString("https://code.forgejo.org/my/repo")

    @TestFactory
    fun `should generate correct URLs`() = assertUrls(
        TemplatedUrlFactory(Forgejo().templates),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
            "https://code.forgejo.org/my/repo/src/branch/master/src/Foo.java#L10-L20",
            "File at branch with line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, null),
            "https://code.forgejo.org/my/repo/src/branch/master/src/Foo.java",
            "File at branch without line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(
                File("my-image.png", false, "src/foo bar baz/images", false),
                BRANCH_MASTER,
                null
            ),
            "https://code.forgejo.org/my/repo/src/branch/master/src/foo%20bar%20baz/images/my-image.png",
            "File with spaces in path should be URL encoded"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
            "https://code.forgejo.org/my/repo/src/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java#L10-L20",
            "File at commit with line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(DIR_RESOURCES, "main", COMMIT_FULL, null),
            "https://code.forgejo.org/my/repo/src/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/foo/resources",
            "Directory at commit"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(DIR_ROOT, "main", COMMIT_FULL, null),
            "https://code.forgejo.org/my/repo/src/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Repository root at commit"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, null),
            "https://code.forgejo.org/my/repo/src/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java",
            "File at commit without line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.Commit(COMMIT_FULL, "main"),
            "https://code.forgejo.org/my/repo/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Direct commit URL"
        ),
    )
}
