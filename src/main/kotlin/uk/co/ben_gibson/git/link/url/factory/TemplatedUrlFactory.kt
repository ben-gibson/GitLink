package uk.co.ben_gibson.git.link.url.factory

import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.git.LineSelection
import uk.co.ben_gibson.git.link.url.*
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import uk.co.ben_gibson.url.URL
import java.util.regex.Pattern
import com.google.common.net.UrlEscapers

open class TemplatedUrlFactory(private val templates: UrlTemplates) : UrlFactory {
    protected val pathEscaper: (String) -> String = UrlEscapers.urlPathSegmentEscaper().asFunction()::apply

    // Escaped a segment at a time so that any forward slashes are preserved, as most platforms expect
    // them unescaped in the branch portion of a URL.
    protected open val branchEscaper: (String) -> String = { branch ->
        branch.split("/").joinToString("/") { pathEscaper(it) }
    }

    private val remotePathPattern = Pattern.compile("\\{remote:url:path:(\\d)}")

    override fun createUrl(baseUrl: URL, options: UrlOptions): URL {
        var processTemplate = when (options) {
            is UrlOptions.FileAtCommit -> processTemplate(options)
            is UrlOptions.FileAtBranch -> processTemplate(options)
            is UrlOptions.Commit -> processTemplate(options)
        }

        processTemplate = processBaseUrl(processTemplate, baseUrl)
        processTemplate = removeUnmatchedSubstitutions(processTemplate)
        processTemplate = processTemplate.replace("(?<!:)/{2,}".toRegex(), "/")

        return URL.fromString(processTemplate)
    }

    private fun removeUnmatchedSubstitutions(template: String) = template.replace("\\{.+?}".toRegex(), "")

    private fun processTemplate(options: UrlOptions.FileAtBranch): String {
        var template = templates.fileAtBranch

        template = processFile(template, options.file)
        template = processBranch(template, options.branch)
        template = processLineSelection(template, options.lineSelection, options.file)

        return template
    }

    private fun processTemplate(options: UrlOptions.FileAtCommit): String {
        var template = templates.fileAtCommit

        template = processFile(template, options.file)
        template = processCommit(template, options.commit)
        template = processBranch(template, options.currentBranch)
        template = processLineSelection(template, options.lineSelection, options.file)

        return template
    }

    private fun processTemplate(options: UrlOptions.Commit): String {
        var template = templates.commit

        template = processCommit(template, options.commit)
        template = processBranch(template, options.currentBranch)

        return template
    }

    private fun processBaseUrl(template: String, baseUrl: URL) : String {
        var processed = template
            .replace("{remote:url:protocol}", baseUrl.scheme.toString())
            .replace("{remote:url:host}", baseUrl.host.toString())
            .replace("{remote:url}", baseUrl.toString())
            .replace("{remote:url:path}", baseUrl.path.toString())

        val pathParts = baseUrl.path.toString().split("/")

        val remotePathMatcher = remotePathPattern.matcher(template)

        while (remotePathMatcher.find()) {
            val position = remotePathMatcher.group(1).toInt()
            processed = processed.replace("{remote:url:path:${position}}", pathParts.getOrElse(position) { "" })
        }

        return processed
    }

    private fun processBranch(template: String, branch: String) = template
        .replace("{branch}", branchEscaper(branch))

    private fun processFile(template: String, file: File) = template
        .replace("{object}", if (file.isDirectory) "tree" else "blob")
        .replace("{file:name}", if (file.isRoot) "" else pathEscaper(file.name))
        .replace("{file:path}", file.path.split("/").joinToString("/") { pathEscaper(it) })

    private fun processCommit(template: String, commit: Commit) = template
        .replace("{commit}", commit.toString())
        .replace("{commit:short}", commit.shortHash)

    private fun processLineSelection(template: String, lineSelection: LineSelection?, file: File) : String {
        if (lineSelection == null || file.isDirectory) {
            return template.replace("\\{line-block:start}.+\\{line-block:end}".toRegex(), "")
        }

        var processed = template

        // Whole file selection
        if (lineSelection.start == 1) {
            return processed.replace("\\{line-block:start}.+\\{line-block:end}".toRegex(), "")
        }

        // Single line selection
        if (lineSelection.start == lineSelection.end) {
            processed = processed.replace("-L{line:end}", "")
            processed = processed.replace("-{line:end}", "")
            processed = processed.replace(":{line:end}", "")
        }

        return processed
            .replace("{line:start}", lineSelection.start.toString())
            .replace("{line:end}", lineSelection.end.toString())
    }
}