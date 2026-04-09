package uk.co.ben_gibson.git.link.url

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_MASTER
import uk.co.ben_gibson.git.link.url.UrlTestData.COMMIT_FULL
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_RESOURCES
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_ROOT
import uk.co.ben_gibson.git.link.url.UrlTestData.FILE_JAVA
import uk.co.ben_gibson.git.link.url.UrlTestData.LINE_SELECTION_RANGE
import uk.co.ben_gibson.git.link.url.factory.AzureUrlFactory
import uk.co.ben_gibson.url.URL
import java.util.stream.Stream

class AzureTest {

    companion object {
        private val BASE_URL_WITH_GIT = URL.fromString("https://dev.azure.com/ben-gibson/_git/test")
        private val BASE_URL_WITHOUT_GIT = URL.fromString("https://dev.azure.com/ben-gibson/test")
        private val BASE_URL_COMPANY_WITH_GIT = URL.fromString("https://dev.azure.com/company/project/_git/test")
        private val BASE_URL_COMPANY_WITHOUT_GIT = URL.fromString("https://dev.azure.com/company/project/test")

        @JvmStatic
        fun urlExpectations(): Stream<Arguments> = Stream.of(
            Arguments.of(
                BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
                "https://dev.azure.com/ben-gibson/_git/test?version=GBmaster&path=src%2FFoo.java&line=10&lineEnd=21&lineStartColumn=1&lineEndColumn=1",
                "File at branch with line selection (with _git)"
            ),
            Arguments.of(
                BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtBranch(FILE_JAVA, BRANCH_MASTER, null),
                "https://dev.azure.com/ben-gibson/_git/test?version=GBmaster&path=src%2FFoo.java",
                "File at branch without line selection"
            ),
            Arguments.of(
                BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtCommit(FILE_JAVA, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
                "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2FFoo.java&line=10&lineEnd=21&lineStartColumn=1&lineEndColumn=1",
                "File at commit with line selection"
            ),
            Arguments.of(
                BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtCommit(DIR_RESOURCES, "main", COMMIT_FULL, null),
                "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2Ffoo%2Fresources",
                "Directory at commit"
            ),
            Arguments.of(
                BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtCommit(DIR_ROOT, "main", COMMIT_FULL, null),
                "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=%2F",
                "Repository root at commit"
            ),
            Arguments.of(
                BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsFileAtCommit(FILE_JAVA, "main", COMMIT_FULL, null),
                "https://dev.azure.com/ben-gibson/_git/test?version=GCb032a0707beac9a2f24b1b7d97ee4f7156de182c&path=src%2FFoo.java",
                "File at commit without line selection"
            ),
            Arguments.of(
                BASE_URL_WITH_GIT,
                UrlOptions.UrlOptionsCommit(COMMIT_FULL, "main"),
                "https://dev.azure.com/ben-gibson/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Direct commit URL (with _git)"
            ),
            Arguments.of(
                BASE_URL_WITHOUT_GIT,
                UrlOptions.UrlOptionsCommit(COMMIT_FULL, "main"),
                "https://dev.azure.com/ben-gibson/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Direct commit URL (without _git should add it)"
            ),
            Arguments.of(
                BASE_URL_COMPANY_WITH_GIT,
                UrlOptions.UrlOptionsCommit(COMMIT_FULL, "main"),
                "https://dev.azure.com/company/project/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Company URL with _git"
            ),
            Arguments.of(
                BASE_URL_COMPANY_WITHOUT_GIT,
                UrlOptions.UrlOptionsCommit(COMMIT_FULL, "main"),
                "https://dev.azure.com/company/project/_git/test/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Company URL without _git"
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
        val factory = AzureUrlFactory()

        // When
        val url = factory.createUrl(baseUrl, options)

        // Then
        assertThat(url.toString())
            .describedAs(description)
            .isEqualTo(expectedUrl)
    }
}
