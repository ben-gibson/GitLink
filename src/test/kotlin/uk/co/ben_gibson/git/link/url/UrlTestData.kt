package uk.co.ben_gibson.git.link.url

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.git.LineSelection
import uk.co.ben_gibson.git.link.url.factory.UrlFactory
import uk.co.ben_gibson.url.URL

object UrlTestData {
    const val BRANCH_MAIN = "main"
    const val BRANCH_MASTER = "master"
    const val BRANCH_WITH_SLASH = "feature/ticket-23"
    const val BRANCH_WITH_SPACE = "feature/ticket 23"

    val COMMIT_FULL = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")

    val FILE_JAVA = File("Foo.java", false, "src", false)

    val DIR_RESOURCES = File("resources", true, "src/foo", false)
    val DIR_ROOT = File("my-project", true, "", true)

    val LINE_SELECTION_RANGE = LineSelection(10, 20)

    class Expectation(val baseUrl: URL, val options: UrlOptions, val expectedUrl: String, val description: String)

    fun assertUrls(factory: UrlFactory, vararg expectations: Expectation): List<DynamicTest> = expectations.map {
        dynamicTest(it.description) {
            assertThat(factory.createUrl(it.baseUrl, it.options).toString()).isEqualTo(it.expectedUrl)
        }
    }
}
