package uk.co.ben_gibson.git.link.url

import com.google.common.net.UrlEscapers

fun encode(value: String): String = UrlEscapers.urlPathSegmentEscaper().escape(value)