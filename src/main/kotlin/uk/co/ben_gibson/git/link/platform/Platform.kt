package uk.co.ben_gibson.git.link.platform

import com.intellij.icons.AllIcons
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.ui.Icons
import java.util.UUID
import javax.swing.Icon
import uk.co.ben_gibson.url.Host

sealed class Platform(
    val id: UUID,
    val name: String,
    val icon: Icon,
    val domains: Set<Domain> = setOf(),
    val commitsReachableFromRemote: Boolean = true
) {
    fun matches(host: Host) = domains.any { it.matches(host) }

    fun matchesExactly(host: Host) = domains.any { !it.isWildcard && it.matches(host) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Platform

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

class GitHub : Platform(
    UUID.fromString("72037fcc-cb9c-4c22-960a-ffe73fd5e229"),
    message("platform.github.name"),
    AllIcons.Vcs.Vendors.Github,
    setOf(Domain.of("github.com"))
)

class GitLab : Platform(
    UUID.fromString("16abfb4c-4717-4d04-a8f1-7a40fcac9b07"),
    message("platform.gitlab.name"),
    Icons.GITLAB,
    setOf(Domain.of("gitlab.com"))
)

class BitbucketCloud : Platform(
    UUID.fromString("00c4b661-b32a-4d36-90d7-88db786edadd"),
    message("platform.bitbucket.cloud.name"),
    Icons.BITBUCKET,
    setOf(Domain.of("bitbucket.org"))
)

class BitbucketServer : Platform(
    UUID.fromString("dba5941d-821c-49b3-83b0-75deb9462acb"),
    message("platform.bitbucket.server.name"),
    Icons.BITBUCKET,
    setOf(Domain.wildcard("bitbucket"))
)

class Gogs : Platform(
    UUID.fromString("fd2d9cfc-1eef-4b1b-80bd-b02def58576c"),
    message("platform.gogs.name"),
    Icons.GOGS,
    setOf(Domain.of("gogs.io"))
)

class Srht : Platform(
    UUID.fromString("aa358239-5c11-4b53-8b97-723181c48f4f"),
    message("platform.srht.name"),
    Icons.SOURCEHUT,
    setOf(Domain.of("git.sr.ht"))
)

class Gitea : Platform(
    UUID.fromString("e0f86390-1091-4871-8aeb-f534fbc99cf0"),
    message("platform.gitea.name"),
    Icons.GITEA,
    setOf(Domain.of("gitea.io")),
)

class Gitee : Platform(
    UUID.fromString("5c2d3009-7e3e-4c9f-9c0f-d76bc7e926bf"),
    message("platform.gitee.name"),
    Icons.GITEE,
    setOf(Domain.of("gitee.com"))
)

class Azure : Platform(
    UUID.fromString("83008277-73fa-4faa-b9b2-0a60fecb030e"),
    message("platform.azure.name"),
    Icons.AZURE,
    setOf(Domain.of("dev.azure.com"), Domain.wildcard("azure"))
)

class Chromium : Platform(
    UUID.fromString("97bf87bc-99ef-4e1f-8d37-7948a2082df4"),
    message("platform.chromium.name"),
    Icons.CHROMIUM,
    setOf(Domain.of("googlesource.com"))
)

// Gerrit changes are pushed to refs/for/<branch> rather than to the branch itself, so a local commit
// is never reachable from a remote branch and checking for one would always fall back to a branch link.
class Gerrit : Platform(
    UUID.fromString("a28d7024-f390-40d1-8554-db65a9120a38"),
    message("platform.gerrit.name"),
    Icons.GERRIT,
    setOf(Domain.wildcard("gerrit")),
    commitsReachableFromRemote = false
)

class Codeberg : Platform(
    UUID.fromString("3fc8e330-760f-482f-8758-a0c34137d21c"),
    message("platform.codeberg.name"),
    Icons.CODEBERG,
    setOf(Domain.of("codeberg.org"))
)

class Custom(id: UUID, name: String, icon: Icon, domains: Set<Domain> = setOf()) : Platform(id, name, icon, domains)
