package uk.co.ben_gibson.git.link.url

import org.junit.jupiter.api.TestFactory
import uk.co.ben_gibson.git.link.platform.BitbucketCloud
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_MASTER
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_WITH_SLASH
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_WITH_SPACE
import uk.co.ben_gibson.git.link.url.UrlTestData.COMMIT_FULL
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_RESOURCES
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_ROOT
import uk.co.ben_gibson.git.link.url.UrlTestData.Expectation
import uk.co.ben_gibson.git.link.url.UrlTestData.FILE_JAVA
import uk.co.ben_gibson.git.link.url.UrlTestData.LINE_SELECTION_RANGE
import uk.co.ben_gibson.git.link.url.UrlTestData.assertUrls
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import uk.co.ben_gibson.url.URL

class BitBucketCloudTest {
    private val baseUrl = URL.fromString("https://bitbucket.org/foo/bar")

    @TestFactory
    fun `should generate correct URLs`() = assertUrls(
        TemplatedUrlFactory(BitbucketCloud().templates),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
            "https://bitbucket.org/foo/bar/src/master/src/Foo.java#lines-10:20",
            "File at branch with line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_WITH_SLASH, LINE_SELECTION_RANGE),
            "https://bitbucket.org/foo/bar/src/feature/ticket-23/src/Foo.java#lines-10:20",
            "Branch containing a forward slash should not be encoded"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_WITH_SPACE, LINE_SELECTION_RANGE),
            "https://bitbucket.org/foo/bar/src/feature/ticket%2023/src/Foo.java#lines-10:20",
            "Branch containing a space should be encoded"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, null),
            "https://bitbucket.org/foo/bar/src/master/src/Foo.java",
            "File at branch without line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
            "https://bitbucket.org/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java#lines-10:20",
            "File at commit with line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(DIR_RESOURCES, "main", COMMIT_FULL, null),
            "https://bitbucket.org/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/foo/resources",
            "Directory at commit"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(DIR_ROOT, "main", COMMIT_FULL, null),
            "https://bitbucket.org/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Repository root at commit"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, null),
            "https://bitbucket.org/foo/bar/src/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java",
            "File at commit without line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.Commit(COMMIT_FULL, "main"),
            "https://bitbucket.org/foo/bar/commits/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Direct commit URL"
        ),
    )
}
