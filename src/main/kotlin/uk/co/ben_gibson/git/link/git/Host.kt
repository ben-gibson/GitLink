package uk.co.ben_gibson.git.link.git

import uk.co.ben_gibson.git.link.url.template.UrlTemplate
import java.net.URL
import java.util.UUID
import javax.swing.Icon

val HOST_ID_GITHUB: UUID = UUID.fromString("72037fcc-cb9c-4c22-960a-ffe73fd5e229")
val HOST_ID_GITLAB: UUID = UUID.fromString("16abfb4c-4717-4d04-a8f1-7a40fcac9b07")
val HOST_ID_BITBUCKET_CLOUD: UUID = UUID.fromString("00c4b661-b32a-4d36-90d7-88db786edadd")
val HOST_ID_BITBUCKET_SERVER: UUID = UUID.fromString("dba5941d-821c-49b3-83b0-75deb9462acb")
val HOST_ID_GOGS: UUID = UUID.fromString("fd2d9cfc-1eef-4b1b-80bd-b02def58576c")
val HOST_ID_GITEA: UUID = UUID.fromString("e0f86390-1091-4871-8aeb-f534fbc99cf0")
val HOST_ID_AZURE: UUID = UUID.fromString("83008277-73fa-4faa-b9b2-0a60fecb030e")

sealed class Host(val id: UUID, val displayName: String, val icon: Icon, val baseUrl: URL? = null)

class TemplatedHost(
    id: UUID,
    displayName: String,
    icon: Icon,
    val urlTemplate: UrlTemplate,
    baseUrl: URL? = null
) : Host(id, displayName, icon, baseUrl)
