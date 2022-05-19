package uk.co.ben_gibson.git.link.git

import com.intellij.icons.AllIcons
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.ui.Icons
import java.net.URI
import java.util.UUID
import javax.swing.Icon

sealed class Host(val id: UUID, val displayName: String, val icon: Icon, val domains: Set<URI> = setOf())

class GitHub() : Host(
    UUID.fromString("72037fcc-cb9c-4c22-960a-ffe73fd5e229"),
    message("hosts.github.name"),
    AllIcons.Vcs.Vendors.Github,
    setOf(URI("github.com"))
)

class GitLab() : Host(
    UUID.fromString("16abfb4c-4717-4d04-a8f1-7a40fcac9b07"),
    message("hosts.gitlab.name"),
    Icons.GITLAB,
    setOf(URI("gitlab.com"))
)

class BitbucketCloud() : Host(
    UUID.fromString("00c4b661-b32a-4d36-90d7-88db786edadd"),
    message("hosts.bitbucket.cloud.name"),
    Icons.BITBUCKET,
    setOf(URI("bitbucket.org"))
)

class BitbucketServer() : Host(
    UUID.fromString("dba5941d-821c-49b3-83b0-75deb9462acb"),
    message("hosts.bitbucket.server.name"),
    Icons.BITBUCKET
)

class Gogs() : Host(
    UUID.fromString("fd2d9cfc-1eef-4b1b-80bd-b02def58576c"),
    message("hosts.gogs.name"),
    Icons.GOGS,
    setOf(URI("gogs.io"))
)

class Gitea() : Host(
    UUID.fromString("e0f86390-1091-4871-8aeb-f534fbc99cf0"),
    message("hosts.gitea.name"),
    Icons.GITEA,
    setOf(URI("gitea.io")),
)

class Gitee() : Host(
    UUID.fromString("5c2d3009-7e3e-4c9f-9c0f-d76bc7e926bf"),
    message("hosts.gitee.name"),
    Icons.GITEE,
    setOf(URI("gitee.com"))
)

class Azure() : Host(
    UUID.fromString("83008277-73fa-4faa-b9b2-0a60fecb030e"),
    message("hosts.azure.name"),
    Icons.AZURE,
    setOf(URI("dev.azure.com"))
)

class Chromium() : Host(
    UUID.fromString("97bf87bc-99ef-4e1f-8d37-7948a2082df4"),
    message("hosts.chromium.name"),
    Icons.CHROMIUM,
    setOf(URI("googlesource.com"))
)

class Custom(id: UUID, displayName: String, icon: Icon, domains: Set<URI> = setOf()) : Host(id, displayName, icon, domains)