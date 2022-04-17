package uk.co.ben_gibson.git.link.url.factory

import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.*
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import java.net.URI
import java.net.URL
import java.util.regex.Pattern


class TemplatedUrlFactory(private val templates: UrlTemplates) : UrlFactory {
    private val remotePathPattern = Pattern.compile("\\{remote:url:path:(\\d)}")

    override fun createUrl(options: UrlOptions): URL {
        var processTemplate = when (options) {
            is UrlOptionsFileAtCommit -> processTemplate(options)
            is UrlOptionsFileAtBranch -> processTemplate(options)
            is UrlOptionsCommit -> processTemplate(options)
        }

        processTemplate = processBaseUrl(processTemplate, options.baseUrl)
        processTemplate = removeUnmatchedSubstitutions(processTemplate)

        val url = URL(processTemplate)

        val uri = URI(
            url.protocol,
            null,
            url.host,
            url.port,
            url.path.replace("/{2,}".toRegex(), "/").trimEnd('/'),
            url.query,
            url.ref
        );

        return URL(uri.toASCIIString())
    }

    private fun removeUnmatchedSubstitutions(template: String) = template.replace("\\{.+?}".toRegex(), "")

    private fun processTemplate(options: UrlOptionsFileAtBranch): String {
        var template = templates.fileAtBranch

        template = processFile(template, options.file)
        template = processBranch(template, options.branch)
        template = processLineSelection(template, options.lineSelection, options.file)

        return template
    }

    private fun processTemplate(options: UrlOptionsFileAtCommit): String {
        var template = templates.fileAtCommit

        template = processFile(template, options.file)
        template = processCommit(template, options.commit)
        template = processLineSelection(template, options.lineSelection, options.file)

        return template
    }

    private fun processTemplate(options: UrlOptionsCommit): String {
        var template = templates.commit

        template = processCommit(template, options.commit)

        return template
    }

    private fun processBaseUrl(template: String, baseUrl: URL) : String {
        var processed = template
            .replace("{remote:url:host}", baseUrl.hostUrl.toString())
            .replace("{remote:url}", baseUrl.toString())
            .replace("{remote:url:path}", baseUrl.path)

        val pathParts = baseUrl.path.trimStart('/').split("/")

        val remotePathMatcher = remotePathPattern.matcher(template)

        while (remotePathMatcher.find()) {
            val position = remotePathMatcher.group(1).toInt()
            processed = processed.replace("{remote:url:path:${position}}", pathParts.getOrElse(position) { "" })
        }

        return processed;
    }

    private fun processBranch(template: String, branch: String) = template
        .replace("{branch}", branch)

    private fun processFile(template: String, file: File) = template
        .replace("{object}", if (file.isDirectory) "tree" else "blob")
        .replace("{file:name}", if (file.isRoot) "" else file.name)
        .replace("{file:path}", file.path)

    private fun processCommit(template: String, commit: Commit) = template
        .replace("{commit}", commit.toString())
        .replace("{commit:short}", commit.shortHash)

    private fun processLineSelection(template: String, lineSelection: LineSelection?, file: File) : String {
        if (lineSelection == null || file.isDirectory) {
            return template.replace("\\{line-block:start}.+\\{line-block:end}".toRegex(), "")
        }

        return template
            .replace("{line:start}", lineSelection.start.toString())
            .replace("{line:end}", lineSelection.end.toString())
    }
}