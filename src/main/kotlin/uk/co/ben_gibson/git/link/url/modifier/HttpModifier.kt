package uk.co.ben_gibson.git.link.url.modifier

import com.intellij.openapi.components.Service
import java.net.URL

@Service
class HttpModifier : UrlModifier {
    override fun modify(url: URL) = url
}