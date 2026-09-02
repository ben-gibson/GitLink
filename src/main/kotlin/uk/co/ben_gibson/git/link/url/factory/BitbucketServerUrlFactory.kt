package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.url.UrlOptions
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import uk.co.ben_gibson.url.Path
import uk.co.ben_gibson.url.URL

private val TEMPLATES = UrlTemplates(
    "{remote:url:protocol}://{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at=refs/heads/{branch}{line-block:start}#{line:start}-{line:end}{line-block:end}",
    "{remote:url:protocol}://{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at={commit}{line-block:start}#{line:start}-{line:end}{line-block:end}",
    "{remote:url:protocol}://{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/commits/{commit}"
)

// The templates live here rather than on the platform because they only hold once the 'scm/' prefix has
// been stripped from the remote URL, which is work a custom platform could not reproduce.
@Service
class BitbucketServerUrlFactory : TemplatedUrlFactory(TEMPLATES) {
    // The branch sits in the 'at' query parameter rather than the path, so forward slashes are escaped.
    override val branchEscaper: (String) -> String = { pathEscaper(it) }

    override fun createUrl(baseUrl: URL, options: UrlOptions): URL {
        return super.createUrl(normaliseBaseUrl(baseUrl), options)
    }

    private fun normaliseBaseUrl(baseUrl: URL): URL {
        return baseUrl.copy(path = Path(baseUrl.path.toString().removePrefix("scm/")))
    }
}
