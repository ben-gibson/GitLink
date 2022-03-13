package uk.co.ben_gibson.git.link.url.factory

import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.*
import uk.co.ben_gibson.git.link.url.template.UrlTemplate
import java.net.URL

class TemplatedUrlFactory(private val templateConfiguration: UrlTemplate) : UrlFactory {
    override fun createUrl(options: UrlOptions): URL {
        var processTemplate = when (options) {
            is UrlOptionsFileAtCommit -> processCommitTemplate(options)
            is UrlOptionsFileAtBranch -> processCommitTemplate(options)
            is UrlOptionsCommit -> processCommitTemplate(options)
        }

        processTemplate = processBaseUrl(processTemplate, options.baseUrl)

        return URL(processTemplate)
    }

    private fun processCommitTemplate(options: UrlOptionsFileAtBranch): String {
        var template = templateConfiguration.fileAtBranch

        template = processFile(template, options.file)
        template = processBranch(template, options.branch)

        options.lineSelection?.let { template = processLineSelection(template, it) }

        return template
    }

    private fun processCommitTemplate(options: UrlOptionsFileAtCommit): String {
        var template = templateConfiguration.fileAtCommit

        template = processFile(template, options.file)
        template = processCommit(template, options.commit)

        options.lineSelection?.let { template = processLineSelection(template, it) }

        return template
    }

    private fun processCommitTemplate(options: UrlOptionsCommit): String {
        var template = templateConfiguration.commit

        template = processCommit(template, options.commit)

        options.lineSelection?.let { template = processLineSelection(template, it) }

        return template
    }

    private fun processBaseUrl(template: String, baseUrl: URL) = template
        .replace("{remote:url:host}", baseUrl.hostUrl.toString())
        .replace("{remote:url}", baseUrl.toString())
        .replace("{remote:url:path}", baseUrl.path)

    private fun processBranch(template: String, branch: String) = template
        .replace("{branch}", branch)

    private fun processFile(template: String, file: File) = template
        .replace("{file:name}", file.name)
        .replace("{file:path}", file.path)

    private fun processCommit(template: String, commit: Commit) = template
        .replace("{commit}", commit.toString())
        .replace("{commit:short}", commit.shortHash)

    private fun processLineSelection(template: String, lineSelection: LineSelection) = template
        .replace("{line:start}", lineSelection.start.toString())
        .replace("{line:end}", lineSelection.end.toString())
}