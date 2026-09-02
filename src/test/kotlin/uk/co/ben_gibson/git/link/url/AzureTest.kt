package uk.co.ben_gibson.git.link.url

import org.junit.jupiter.api.TestFactory
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_MASTER
import uk.co.ben_gibson.git.link.url.UrlTestData.COMMIT_FULL
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_RESOURCES
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_ROOT
import uk.co.ben_gibson.git.link.url.UrlTestData.Expectation
import uk.co.ben_gibson.git.link.url.UrlTestData.FILE_JAVA
import uk.co.ben_gibson.git.link.url.UrlTestData.LINE_SELECTION_RANGE
import uk.co.ben_gibson.git.link.url.UrlTestData.assertUrls
import uk.co.ben_gibson.git.link.url.factory.AzureUrlFactory
import uk.co.ben_gibson.url.URL

class AzureTest {
    private val baseUrlWithGit = URL.fromString("https://dev.azure.com/ben-gibson/_git/test")
    private val baseUrlWithoutGit = URL.fromString("https://dev.azure.com/ben-gibson/test")
    private val baseUrlCompanyWithGit = URL.fromString("https://dev.azure.com/company/project/_git/test")
    private val baseUrlCompanyWithoutGit = URL.fromString("https://dev.azure.com/company/project/test")

    @TestFactory
    fun `should generate correct URLs`() = assertUrls(
        AzureUrlFactory(),
        Expectation(
            baseUrlWithGit,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
            "https://dev.azure.com/ben-gibson/_git/test?version=GBmaster&path=src%2FFoo.java&line=10&lineEnd=21&lineStartColumn=1&lineEndColumn=1",
            "File at branch with line selection (with _git)"
        ),
        Expectation(
            baseUrlWithGit,
            UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, null),
            "https://dev.azure.com/ben-gibson/_git/test?version=GBmaster&path=src%2FFoo.java",
            "File at branch without line selection"
        ),
        Expectation(
            baseUrlWithGit,
            UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
            "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2FFoo.java&line=10&lineEnd=21&lineStartColumn=1&lineEndColumn=1",
            "File at commit with line selection"
        ),
        Expectation(
            baseUrlWithGit,
            UrlOptions.FileAtCommit(DIR_RESOURCES, "main", COMMIT_FULL, null),
            "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2Ffoo%2Fresources",
            "Directory at commit"
        ),
        Expectation(
            baseUrlWithGit,
            UrlOptions.FileAtCommit(DIR_ROOT, "main", COMMIT_FULL, null),
            "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=%2F",
            "Repository root at commit"
        ),
        Expectation(
            baseUrlWithGit,
            UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, null),
            "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2FFoo.java",
            "File at commit without line selection"
        ),
        Expectation(
            baseUrlWithGit,
            UrlOptions.Commit(COMMIT_FULL, "main"),
            "https://dev.azure.com/ben-gibson/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Direct commit URL (with _git)"
        ),
        Expectation(
            baseUrlWithoutGit,
            UrlOptions.Commit(COMMIT_FULL, "main"),
            "https://dev.azure.com/ben-gibson/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Direct commit URL (without _git should add it)"
        ),
        Expectation(
            baseUrlCompanyWithGit,
            UrlOptions.Commit(COMMIT_FULL, "main"),
            "https://dev.azure.com/company/project/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Company URL with _git"
        ),
        Expectation(
            baseUrlCompanyWithoutGit,
            UrlOptions.Commit(COMMIT_FULL, "main"),
            "https://dev.azure.com/company/project/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
            "Company URL without _git"
        ),
    )
}
