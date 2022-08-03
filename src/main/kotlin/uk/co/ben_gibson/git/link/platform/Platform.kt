package uk.co.ben_gibson.git.link.platform

import com.intellij.icons.AllIcons
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.ui.Icons
import java.util.UUID
import javax.swing.Icon
import uk.co.ben_gibson.url.Host;

sealed class Platform(val id: UUID, val name: String, val icon: Icon, val domains: Set<Host> = setOf()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Platform

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

class GitHub() : Platform(
    UUID.fromString("72037fcc-cb9c-4c22-960a-ffe73fd5e229"),
    message("platform.github.name"),
    AllIcons.Vcs.Vendors.Github,
    setOf(Host("github.com"))
)

class GitLab() : Platform(
    UUID.fromString("16abfb4c-4717-4d04-a8f1-7a40fcac9b07"),
    message("platform.gitlab.name"),
    Icons.GITLAB,
    setOf(Host("gitlab.com"))
)

class BitbucketCloud() : Platform(
    UUID.fromString("00c4b661-b32a-4d36-90d7-88db786edadd"),
    message("platform.bitbucket.cloud.name"),
    Icons.BITBUCKET,
    setOf(Host("bitbucket.org"))
)

class BitbucketServer() : Platform(
    UUID.fromString("dba5941d-821c-49b3-83b0-75deb9462acb"),
    message("platform.bitbucket.server.name"),
    Icons.BITBUCKET
)

class Gogs() : Platform(
    UUID.fromString("fd2d9cfc-1eef-4b1b-80bd-b02def58576c"),
    message("platform.gogs.name"),
    Icons.GOGS,
    setOf(Host("gogs.io"))
)

class Gitea() : Platform(
    UUID.fromString("e0f86390-1091-4871-8aeb-f534fbc99cf0"),
    message("platform.gitea.name"),
    Icons.GITEA,
    setOf(Host("gitea.io")),
)

class Gitee() : Platform(
    UUID.fromString("5c2d3009-7e3e-4c9f-9c0f-d76bc7e926bf"),
    message("platform.gitee.name"),
    Icons.GITEE,
    setOf(Host("gitee.com"))
)

class Azure() : Platform(
    UUID.fromString("83008277-73fa-4faa-b9b2-0a60fecb030e"),
    message("platform.azure.name"),
    Icons.AZURE,
    setOf(Host("dev.azure.com"))
)

class Chromium() : Platform(
    UUID.fromString("97bf87bc-99ef-4e1f-8d37-7948a2082df4"),
    message("platform.chromium.name"),
    Icons.CHROMIUM,
    setOf(Host("googlesource.com"))
)

class Custom(id: UUID, name: String, icon: Icon, domains: Set<Host> = setOf()) : Platform(id, name, icon, domains)