package uk.co.ben_gibson.git.link.url

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_MASTER
import uk.co.ben_gibson.git.link.url.UrlTestData.COMMIT_FULL
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_RESOURCES
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_ROOT
import uk.co.ben_gibson.git.link.url.UrlTestData.FILE_JAVA
import uk.co.ben_gibson.git.link.url.UrlTestData.LINE_SELECTION_RANGE
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import uk.co.ben_gibson.url.URL
import java.util.stream.Stream

class GitLabTest {

    companion object {
        private val BASE_URL = URL.fromString("https://gitlab.com/my/repo/")

        @JvmStatic
        fun urlExpectations(): Stream<Arguments> = Stream.of(
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
                "https://gitlab.com/my/repo/blob/master/src/Foo.java#L10-20",
                "File at branch with line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE_JAVA, BRANCH_MASTER, null),
                "https://gitlab.com/my/repo/blob/master/src/Foo.java",
                "File at branch without line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE_JAVA, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
                "https://gitlab.com/my/repo/blob/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java#L10-20",
                "File at commit with line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(
                    File("Code.cs", false, "Assets/#/Sources", false),
                    BRANCH_MASTER,
                    LINE_SELECTION_RANGE
                ),
                "https://gitlab.com/my/repo/blob/master/Assets/%23/Sources/Code.cs#L10-20",
                "File with special characters (#) in path should be URL encoded"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(DIR_RESOURCES, "main", COMMIT_FULL, null),
                "https://gitlab.com/my/repo/tree/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/foo/resources",
                "Directory at commit"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(DIR_ROOT, "main", COMMIT_FULL, null),
                "https://gitlab.com/my/repo/tree/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Repository root at commit"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE_JAVA, "main", COMMIT_FULL, null),
                "https://gitlab.com/my/repo/blob/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java",
                "File at commit without line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsCommit(COMMIT_FULL, "main"),
                "https://gitlab.com/my/repo/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Direct commit URL"
            )
        )
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource("urlExpectations")
    fun `should generate correct GitLab URLs`(
        baseUrl: URL,
        options: UrlOptions,
        expectedUrl: String,
        description: String
    ) {
        // Given
        val factory = TemplatedUrlFactory(UrlTemplates.gitLab())

        // When
        val url = factory.createUrl(baseUrl, options)

        // Then
        assertThat(url.toString())
            .describedAs(description)
            .isEqualTo(expectedUrl)
    }
}

