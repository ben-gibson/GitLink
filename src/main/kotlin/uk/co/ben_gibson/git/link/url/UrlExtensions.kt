package uk.co.ben_gibson.git.link.url

import java.net.URL

val URL.hostUrl:URL get() { return URL(protocol, host, port, "") }