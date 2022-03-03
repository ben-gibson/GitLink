package uk.co.ben_gibson.git.link

import com.intellij.ide.BrowserUtil
import java.net.URL

fun processGitLink() {
    val url = URL("https://google.com")

    BrowserUtil.browse(url)
}