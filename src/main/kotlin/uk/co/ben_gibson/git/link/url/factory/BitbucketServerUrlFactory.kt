package uk.co.ben_gibson.git.link.url.factory

import com.google.common.net.UrlEscapers
import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.url.UrlOptions
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import uk.co.ben_gibson.url.Path
import uk.co.ben_gibson.url.URL

@Service
class BitbucketServerUrlFactory : TemplatedUrlFactory(UrlTemplates.bitbucketServer()) {
    override val branchEscaper: (String) -> String = { UrlEscapers.urlPathSegmentEscaper().asFunction().apply(it) }

    override fun createUrl(baseUrl: URL, options: UrlOptions): URL {
        return super.createUrl(normaliseBaseUrl(baseUrl), options)
    }

    private fun normaliseBaseUrl(baseUrl: URL): URL {
        return baseUrl.copy(path = Path(baseUrl.path.toString().removePrefix("scm/")))
    }
}