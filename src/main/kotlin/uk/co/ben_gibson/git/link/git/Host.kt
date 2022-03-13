package uk.co.ben_gibson.git.link.git

import uk.co.ben_gibson.git.link.url.template.UrlTemplate
import java.net.URL
import javax.swing.Icon

val HOST_ID_GITHUB = HostId("github")
val HOST_ID_GITLAB = HostId("gitlab")
val HOST_ID_BITBUCKET_CLOUD = HostId("bitbucket-cloud")
val HOST_ID_BITBUCKET_SERVER = HostId("bitbucket-server")
val HOST_ID_GOGS = HostId("gogs")
val HOST_ID_GITEA = HostId("gitea")
val HOST_ID_AZURE = HostId("azure")

sealed class Host(val id: HostId, val displayName: String, val icon: Icon, val baseUrl: URL? = null)

class TemplatedHost(
    id: HostId,
    displayName: String,
    icon: Icon,
    val urlTemplate: UrlTemplate,
    baseUrl: URL? = null
) : Host(id, displayName, icon, baseUrl)

data class HostId(private val id: String) {
    override fun toString(): String {
        return id
    }
}
