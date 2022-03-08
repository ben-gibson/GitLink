package uk.co.ben_gibson.git.link.url

import com.intellij.openapi.vfs.VirtualFile
import uk.co.ben_gibson.git.link.git.Commit
import java.net.URL

data class UrlOptions(
    val baseUrl: URL,
    val branch: String,
    val file: VirtualFile? = null,
    val commit: Commit? = null,
    val lineSelection: LineSelection? = null
) {
    val baseUrlHost: URL
        get() {
            return URL(baseUrl.protocol, baseUrl.host, baseUrl.port, "")
        }

    val hasFileAndCommit: Boolean
        get() {
            return file != null && commit != null
        }
}
