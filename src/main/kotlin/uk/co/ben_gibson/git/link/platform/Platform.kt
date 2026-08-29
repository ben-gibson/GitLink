package uk.co.ben_gibson.git.link.platform

import com.intellij.icons.AllIcons
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.ui.Icons
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
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

sealed class TemplatedPlatform(
    id: UUID,
    name: String,
    icon: Icon,
    val templates: UrlTemplates,
    domains: Set<Domain> = setOf(),
    commitsReachableFromRemote: Boolean = true
) : Platform(id, name, icon, domains, commitsReachableFromRemote)

class GitHub : TemplatedPlatform(
    UUID.fromString("72037fcc-cb9c-4c22-960a-ffe73fd5e229"),
    message("platform.github.name"),
    AllIcons.Vcs.Vendors.Github,
    UrlTemplates(
        "{remote:url}/{object}/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/{object}/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    setOf(Domain.of("github.com"))
)

class GitLab : TemplatedPlatform(
    UUID.fromString("16abfb4c-4717-4d04-a8f1-7a40fcac9b07"),
    message("platform.gitlab.name"),
    Icons.GITLAB,
    UrlTemplates(
        "{remote:url}/{object}/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-{line:end}{line-block:end}",
        "{remote:url}/{object}/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    setOf(Domain.of("gitlab.com"))
)

class BitbucketCloud : TemplatedPlatform(
    UUID.fromString("00c4b661-b32a-4d36-90d7-88db786edadd"),
    message("platform.bitbucket.cloud.name"),
    Icons.BITBUCKET,
    UrlTemplates(
        "{remote:url}/src/{branch}/{file:path}/{file:name}{line-block:start}#lines-{line:start}:{line:end}{line-block:end}",
        "{remote:url}/src/{commit}/{file:path}/{file:name}{line-block:start}#lines-{line:start}:{line:end}{line-block:end}",
        "{remote:url}/commits/{commit}"
    ),
    setOf(Domain.of("bitbucket.org"))
)

class BitbucketServer : Platform(
    UUID.fromString("dba5941d-821c-49b3-83b0-75deb9462acb"),
    message("platform.bitbucket.server.name"),
    Icons.BITBUCKET,
    setOf(Domain.wildcard("bitbucket"))
)

class Gogs : TemplatedPlatform(
    UUID.fromString("fd2d9cfc-1eef-4b1b-80bd-b02def58576c"),
    message("platform.gogs.name"),
    Icons.GOGS,
    UrlTemplates(
        "{remote:url}/src/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/src/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    setOf(Domain.wildcard("gogs"))
)

class Srht : TemplatedPlatform(
    UUID.fromString("aa358239-5c11-4b53-8b97-723181c48f4f"),
    message("platform.srht.name"),
    Icons.SOURCEHUT,
    UrlTemplates(
        "{remote:url}/tree/{branch}/item/{file:path}/{file:name}{line-block:start}#L{line:start}{line-block:end}",
        "{remote:url}/tree/{commit}/item/{file:path}/{file:name}{line-block:start}#L{line:start}{line-block:end}",
        "{remote:url}/tree/{commit}"
    ),
    setOf(Domain.of("git.sr.ht"))
)

class Gitea : TemplatedPlatform(
    UUID.fromString("e0f86390-1091-4871-8aeb-f534fbc99cf0"),
    message("platform.gitea.name"),
    Icons.GITEA,
    UrlTemplates(
        "{remote:url}/src/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/src/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    setOf(Domain.wildcard("gitea")),
)

class Gitee : TemplatedPlatform(
    UUID.fromString("5c2d3009-7e3e-4c9f-9c0f-d76bc7e926bf"),
    message("platform.gitee.name"),
    Icons.GITEE,
    UrlTemplates(
        "{remote:url}/{object}/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/{object}/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    setOf(Domain.of("gitee.com"))
)

class Azure : Platform(
    UUID.fromString("83008277-73fa-4faa-b9b2-0a60fecb030e"),
    message("platform.azure.name"),
    Icons.AZURE,
    setOf(Domain.of("dev.azure.com"), Domain.wildcard("azure"))
)

// Gerrit changes are pushed to refs/for/<branch> rather than to the branch itself, so a local commit
// is never reachable from a remote branch and checking for one would always fall back to a branch link.
class Gerrit : TemplatedPlatform(
    UUID.fromString("a28d7024-f390-40d1-8554-db65a9120a38"),
    message("platform.gerrit.name"),
    Icons.GERRIT,
    UrlTemplates(
        "{remote:url:protocol}://{remote:url:host}/plugins/gitiles/{remote:url:path}/+/refs/heads/{branch}/{file:path}/{file:name}{line-block:start}#{line:start}{line-block:end}",
        "{remote:url:protocol}://{remote:url:host}/plugins/gitiles/{remote:url:path}/+/{commit}/{file:path}/{file:name}{line-block:start}#{line:start}{line-block:end}",
        "{remote:url:protocol}://{remote:url:host}/plugins/gitiles/{remote:url:path}/+/{commit}"
    ),
    setOf(Domain.wildcard("gerrit")),
    commitsReachableFromRemote = false
)

class Forgejo : TemplatedPlatform(
    UUID.fromString("a0ac4b94-3c58-4162-9e5a-7ccef514a972"),
    message("platform.forgejo.name"),
    Icons.FORGEJO,
    UrlTemplates(
        "{remote:url}/src/branch/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/src/commit/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    setOf(Domain.wildcard("forgejo"))
)

class Codeberg : TemplatedPlatform(
    UUID.fromString("3fc8e330-760f-482f-8758-a0c34137d21c"),
    message("platform.codeberg.name"),
    Icons.CODEBERG,
    Forgejo().templates,
    setOf(Domain.of("codeberg.org"))
)

class Custom(definition: CustomPlatform) : TemplatedPlatform(
    definition.id,
    definition.name,
    Icons.GIT,
    definition.templates,
    setOf(definition.domain)
)
