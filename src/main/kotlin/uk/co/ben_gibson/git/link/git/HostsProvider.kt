package uk.co.ben_gibson.git.link.git

import com.intellij.icons.AllIcons
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.Icons
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import java.net.URL
import java.util.*

val GIT_HUB = TemplatedHost(
    HOST_ID_GITHUB,
    message("hosts.github.name"),
    AllIcons.Vcs.Vendors.Github,
    UrlTemplates(
        "{remote:url}/{object}/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/{object}/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    URL("https://github.com")
);

val GIT_LAB = TemplatedHost(
    HOST_ID_GITLAB,
    message("hosts.gitlab.name"),
    Icons.GITLAB,
    UrlTemplates(
        "{remote:url}/{object}/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-{line:end}{line-block:end}",
        "{remote:url}/{object}/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    URL("https://gitlab.com")
);

val BITBUCKET_CLOUD = TemplatedHost(
    HOST_ID_BITBUCKET_CLOUD,
    message("hosts.bitbucket.cloud.name"),
    Icons.BITBUCKET,
    UrlTemplates(
        "{remote:url}/src/HEAD/{file:path}/{file:name}?at={branch}{line-block:start}#lines-{line:start}:{line:end}{line-block:end}",
        "{remote:url}/src/{commit}/{file:path}/{file:name}{line-block:start}#lines-{line:start}:{line:end}{line-block:end}",
        "{remote:url}/commits/{commit}"
    ),
    URL("https://bitbucket.org")
);

val BITBUCKET_SERVER = TemplatedHost(
    HOST_ID_BITBUCKET_SERVER,
    message("hosts.bitbucket.server.name"),
    Icons.BITBUCKET,
    UrlTemplates(
        "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at=refs/heads/{branch}{line-block:start}#{line:start}-{line:end}{line-block:end}",
        "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at={commit}{line-block:start}#{line:start}-{line:end}{line-block:end}",
        "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/commits/{commit}"
    )
);

val GITEA = TemplatedHost(
    HOST_ID_GITEA,
    message("hosts.gitea.name"),
    Icons.GITEA,
    UrlTemplates(
        "{remote:url}/src/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/src/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    URL("https://gitea.io"),
);

val GOGS = TemplatedHost(
    HOST_ID_GOGS,
    message("hosts.gogs.name"),
    Icons.GOGS,
    UrlTemplates(
        "{remote:url}/src/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/src/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
        "{remote:url}/commit/{commit}"
    ),
    URL("https://gogs.io")
);

val AZURE = TemplatedHost(
    HOST_ID_AZURE,
    message("hosts.azure.name"),
    Icons.AZURE,
    UrlTemplates(
        "{remote:url}?version=GB{branch}&path=/{file:path}/{file:name}{line-block:start}&line={line:start}&lineEnd={line:end}{line-block:end}&lineStartColumn=1&lineEndColumn=1",
        "{remote:url}?version=GC{commit}&path=/{file:path}/{file:name}{line-block:start}&line={line:start}&lineEnd={line:end}{line-block:end}&lineStartColumn=1&lineEndColumn=1",
        "{remote:url}/commit/{commit}"
    ),
    URL("https://dev.azure.com")
);

val CHROMIUM = ChromiumHost()

val ALL = setOf(
    GIT_HUB,
    GIT_LAB,
    BITBUCKET_CLOUD,
    BITBUCKET_SERVER,
    GITEA,
    GOGS,
    AZURE,
    CHROMIUM
)

@Service
class HostsProvider {
    fun provide(): Hosts {
        val settings = service<ApplicationSettings>()

        val customHosts: List<TemplatedHost> = settings.customHosts.map {
            TemplatedHost(
                UUID.fromString(it.id),
                it.displayName,
                Icons.GIT,
                UrlTemplates(it.fileAtBranchTemplate, it.fileAtCommitTemplate, it.commitTemplate),
                URL(it.baseUrl)
            )
        }

        return Hosts(ALL.plus(customHosts))
    }
}