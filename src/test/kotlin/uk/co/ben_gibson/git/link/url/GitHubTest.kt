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

class GitHubTest {

    companion object {
        private val BASE_URL = URL.fromString("https://github.com/my/repo")

        @JvmStatic
        fun urlExpectations(): Stream<Arguments> = Stream.of(
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
                "https://github.com/my/repo/blob/master/src/Foo.java#L10-L20",
                "File at branch with line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
                "https://github.com/my/repo/blob/master/src/Foo.java#L10-L20",
                "File at branch with line selection (duplicate for verification)"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtBranch(
                    File("my-image.png", false, "src/foo bar baz/images", false),
                    BRANCH_MASTER,
                    null
                ),
                "https://github.com/my/repo/blob/master/src/foo%20bar%20baz/images/my-image.png",
                "File with spaces in path should be URL encoded"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE_JAVA, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
                "https://github.com/my/repo/blob/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java#L10-L20",
                "File at specific commit with line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(DIR_RESOURCES, "main", COMMIT_FULL, null),
                "https://github.com/my/repo/tree/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/foo/resources",
                "Directory at commit should use /tree/ path"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(DIR_ROOT, "main", COMMIT_FULL, null),
                "https://github.com/my/repo/tree/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Repository root should link to tree at commit"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsFileAtCommit(FILE_JAVA, "main", COMMIT_FULL, null),
                "https://github.com/my/repo/blob/b032a0707beac9a2f24b1b7d97ee4f7156de182c/src/Foo.java",
                "File at commit without line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.UrlOptionsCommit(COMMIT_FULL, "main"),
                "https://github.com/my/repo/commit/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Direct commit URL"
            )
        )
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource("urlExpectations")
    fun `should generate correct GitHub URLs`(baseUrl: URL, options: UrlOptions, expectedUrl: String, description: String) {
        // Given
        val factory = TemplatedUrlFactory(UrlTemplates.gitHub())

        // When
        val url = factory.createUrl(baseUrl, options)

        // Then
        assertThat(url.toString())
            .describedAs(description)
            .isEqualTo(expectedUrl)
    }
}
