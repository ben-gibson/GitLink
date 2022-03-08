package uk.co.ben_gibson.git.link.url.factory

import uk.co.ben_gibson.git.link.url.UrlOptions
import uk.co.ben_gibson.git.link.url.template.UrlTemplateConfiguration
import java.net.URL

abstract class TemplatedUrlFactory : UrlFactory {
    override fun createUrl(options: UrlOptions): URL {
        val templateConfiguration = getUrlTemplateConfiguration()

        when {
            options.hasFileAndCommit -> templateConfiguration.fileAtCommit,
            options.hasFileWithoutCommit -> templateConfiguration.fileAtBranch,
            
        }

        var template = templateConfiguration.fileAtCommit

        template = template
            .replace("{remote:url:host}", options.baseUrlHost.toString())
            .replace("{remote:url}", options.baseUrl.toString())
            .replace("{remote:url:path}", options.baseUrl.path)
            .replace("{file:name}", options.file.name)
            .replace("{file:path}", options.file.path)
            .replace("{branch}", options.branch.toString())
            .replace("{commit}", options.commit?.toString() ?: "")
            .replace("{commit:short}", options.commit?.shortHash ?: "")
            .replace("{line:start}", options.lineSelection?.start?.toString() ?: "")
            .replace("{line:end}", options.lineSelection?.end?.toString() ?: "")

        return URL(template)
    }

    abstract fun getUrlTemplateConfiguration() : UrlTemplateConfiguration
}