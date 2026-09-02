package uk.co.ben_gibson.git.link.url

import org.junit.jupiter.api.TestFactory
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
import uk.co.ben_gibson.git.link.url.factory.BitbucketServerUrlFactory
import uk.co.ben_gibson.url.URL

class BitBucketServerTest {
    private val baseUrl = URL.fromString("https://stash.example.com/foo/bar")
    private val baseUrlScm = URL.fromString("https://stash.example.com/scm/foo/bar")

    @TestFactory
    fun `should generate correct URLs`() = assertUrls(
        BitbucketServerUrlFactory(),
        Expectation(
            baseUrlScm,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
            "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/master#10-20",
            "File at branch with line selection (SCM URL format)"
        ),
        Expectation(
            baseUrlScm,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_WITH_SLASH, LINE_SELECTION_RANGE),
            "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/feature%2Fticket-23#10-20",
            "Branch containing a forward slash should be encoded in the at parameter"
        ),
        Expectation(
            baseUrlScm,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_WITH_SPACE, LINE_SELECTION_RANGE),
            "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/feature%2Fticket%2023#10-20",
            "Branch containing a space should be encoded in the at parameter"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
            "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/master#10-20",
            "File at branch with line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, null),
            "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/master",
            "File at branch without line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
            "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c#10-20",
            "File at commit with line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(DIR_RESOURCES, "main", COMMIT_FULL, null),
            "https://stash.example.com/projects/foo/repos/bar/browse/src/foo/resources?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Directory at commit"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(DIR_ROOT, "main", COMMIT_FULL, null),
            "https://stash.example.com/projects/foo/repos/bar/browse?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Repository root at commit"
        ),
        Expectation(
            baseUrl,
            UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, null),
            "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "File at commit without line selection"
        ),
        Expectation(
            baseUrl,
            UrlOptions.Commit(COMMIT_FULL, "main"),
            "https://stash.example.com/projects/foo/repos/bar/commits/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Direct commit URL"
        ),
    )
}
