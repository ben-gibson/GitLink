package uk.co.ben_gibson.git.link.ui.validation

import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.UrlOptions
import uk.co.ben_gibson.git.link.url.UrlOptionsCommit
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtBranch
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtCommit
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import java.net.MalformedURLException
import java.net.URL

private const val DUMMY_TEMPLATE = "https://foo.bar.com"

private val TEST_HOST = URL("https://foo.com/bar/baz")
private val TEST_COMMIT = Commit("734232a3c18f0625843bd161c3f5da272b9d53c1")
private val TEST_FILE = File("foo.kt", false, "src/main", false)
private const val TEST_BRANCH = "master"
private val TEST_LINE = LineSelection(0, 10)

private val TEST_COMMIT_OPTIONS = UrlOptionsCommit(TEST_HOST, TEST_COMMIT);
private val TEST_FILE_AT_COMMIT_OPTIONS = UrlOptionsFileAtCommit(TEST_HOST, TEST_FILE, TEST_COMMIT, TEST_LINE);
private val TEST_FILE_AT_BRANCH_OPTIONS = UrlOptionsFileAtBranch(TEST_HOST, TEST_FILE, TEST_BRANCH, TEST_LINE);

fun isCommitTemplateValid(template: String) = validate(TEST_COMMIT_OPTIONS, UrlTemplates(DUMMY_TEMPLATE, DUMMY_TEMPLATE, template))
fun isFileAtCommitTemplateValid(template: String) = validate(TEST_FILE_AT_COMMIT_OPTIONS, UrlTemplates(DUMMY_TEMPLATE, template, DUMMY_TEMPLATE))
fun isFileAtBranchTemplateValid(template: String) = validate(TEST_FILE_AT_BRANCH_OPTIONS, UrlTemplates(template, DUMMY_TEMPLATE, DUMMY_TEMPLATE))

private fun validate(options: UrlOptions, templates: UrlTemplates): Boolean {
    val factory = TemplatedUrlFactory(templates)

    return try {
        factory.createUrl(options).let { true }
    } catch (e: MalformedURLException) {
        false
    }
}