package uk.co.ben_gibson.git.link.url

import java.net.URL

val URL.hostUrl:URL get() { return URL(protocol, host, port, "") }
fun URL.toHttps(): URL {
    if (protocol == "https") {
        return this
    }

    var normalised = file

    ref?.let {
        normalised = normalised.plus("#".plus(ref))
    }

    return URL("https", host, port, normalised)
}

fun URL.trimPath(): URL {
    if (!path.endsWith("/")) {
        return this;
    }

    var normalisedFile = path.trimEnd('/')

    query?.let {
        normalisedFile = normalisedFile.plus("?${query}")
    }

    ref?.let {
        normalisedFile = normalisedFile.plus("#${ref}")
    }

    return URL(protocol, host, port, normalisedFile)
}