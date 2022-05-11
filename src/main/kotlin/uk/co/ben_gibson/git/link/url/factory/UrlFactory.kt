package uk.co.ben_gibson.git.link.url.factory

import uk.co.ben_gibson.git.link.url.UrlOptions
import java.net.URI

interface UrlFactory {
    fun createUrl(options: UrlOptions) : URI
}