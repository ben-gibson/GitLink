package uk.co.ben_gibson.git.link.url

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_MASTER
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_WITH_SLASH
import uk.co.ben_gibson.git.link.url.UrlTestData.BRANCH_WITH_SPACE
import uk.co.ben_gibson.git.link.url.UrlTestData.COMMIT_FULL
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_RESOURCES
import uk.co.ben_gibson.git.link.url.UrlTestData.DIR_ROOT
import uk.co.ben_gibson.git.link.url.UrlTestData.FILE_JAVA
import uk.co.ben_gibson.git.link.url.UrlTestData.LINE_SELECTION_RANGE
import uk.co.ben_gibson.git.link.url.factory.BitbucketServerUrlFactory
import uk.co.ben_gibson.url.URL
import java.util.stream.Stream

class BitBucketServerTest {

    companion object {
        private val BASE_URL = URL.fromString("https://stash.example.com/foo/bar")
        private val BASE_URL_SCM = URL.fromString("https://stash.example.com/scm/foo/bar")

        @JvmStatic
        fun urlExpectations(): Stream<Arguments> = Stream.of(
            Arguments.of(
                BASE_URL_SCM,
                UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/master#10-20",
                "File at branch with line selection (SCM URL format)"
            ),
            Arguments.of(
                BASE_URL_SCM,
                UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_WITH_SLASH, LINE_SELECTION_RANGE),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/feature%2Fticket-23#10-20",
                "Branch containing a forward slash should be encoded in the at parameter"
            ),
            Arguments.of(
                BASE_URL_SCM,
                UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_WITH_SPACE, LINE_SELECTION_RANGE),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/feature%2Fticket%2023#10-20",
                "Branch containing a space should be encoded in the at parameter"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, LINE_SELECTION_RANGE),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/master#10-20",
                "File at branch with line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.FileAtBranch(FILE_JAVA, BRANCH_MASTER, null),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=refs/heads/master",
                "File at branch without line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, LINE_SELECTION_RANGE),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c#10-20",
                "File at commit with line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.FileAtCommit(DIR_RESOURCES, "main", COMMIT_FULL, null),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/foo/resources?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Directory at commit"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.FileAtCommit(DIR_ROOT, "main", COMMIT_FULL, null),
                "https://stash.example.com/projects/foo/repos/bar/browse?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "Repository root at commit"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.FileAtCommit(FILE_JAVA, "main", COMMIT_FULL, null),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Foo.java?at=b032a0707beac9a2f24b1b7d97ee4f7156de182c",
                "File at commit without line selection"
            ),
            Arguments.of(
                BASE_URL,
                UrlOptions.Commit(COMMIT_FULL, "main"),
                "https://stash.example.com/projects/foo/repos/bar/commits/b032a0707beac9a2f24b1b7d97ee4f7156de182c",
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
        val factory = BitbucketServerUrlFactory()

        // When
        val url = factory.createUrl(baseUrl, options)

        // Then
        assertThat(url.toString())
            .describedAs(description)
            .isEqualTo(expectedUrl)
    }
}
