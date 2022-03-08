package uk.co.ben_gibson.git.link.url.modifier

import java.net.URL

interface UrlModifier {
    public fun modify(url: URL) : URL
}