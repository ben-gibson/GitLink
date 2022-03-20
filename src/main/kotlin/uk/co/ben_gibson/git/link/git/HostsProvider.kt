package uk.co.ben_gibson.git.link.git

import com.intellij.icons.AllIcons
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.Settings
import uk.co.ben_gibson.git.link.ui.Icons
import uk.co.ben_gibson.git.link.url.template.UrlTemplate
import java.net.URL
import java.util.*

@Service
class HostsProvider(private val project: Project) {

    companion object {
        val EXISTING = setOf(
            TemplatedHost(
                HOST_ID_GITHUB,
                "GitHub",
                AllIcons.Vcs.Vendors.Github,
                UrlTemplate(
                    "{remote:url}/{object}/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
                    "{remote:url}/{object}/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
                    "{remote:url}/commit/{commit}"
                ),
                URL("https://github.com")
            ),
            TemplatedHost(
                HOST_ID_GITLAB,
                "GitLab",
                Icons.GITLAB,
                UrlTemplate(
                    "{remote:url}/blob/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-{line:end}{line-block:end}",
                    "{remote:url}/blob/{commit}/{file:path}/{file:name}#L{line:start}-{line:end}",
                    "{remote:url}/commit/{commit}"
                ),
                URL("https://gitlab.com")
            ),
            TemplatedHost(
                HOST_ID_BITBUCKET_CLOUD,
                "Bitbucket Cloud",
                Icons.BITBUCKET,
                UrlTemplate(
                    "{remote:url}/src/HEAD/{file:path}/{file:name}?at={branch}#lines-{line:start}:{line:end}",
                    "{remote:url}/src/{commit}/{file:path}/{file:name}#lines-{line:start}:{line:end}",
                    "{remote:url}/commits/{commit}"
                ),
                URL("https://bitbucket.org")
            ),
            TemplatedHost(
                HOST_ID_BITBUCKET_SERVER,
                "Bitbucket Server",
                Icons.BITBUCKET,
                UrlTemplate(
                    "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at=refs/heads/{branch}#{line:start}-{line:end}",
                    "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at={commit}#{line:start}-{line:end}",
                    "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/commits/{commit}"
                )
            ),
            TemplatedHost(
                HOST_ID_GITEA,
                "Gitea",
                Icons.GITEA,
                UrlTemplate(
                    "{remote:url}/src/{branch}/{file:path}/{file:name}#L{line:start}-L{line:end}",
                    "{remote:url}/src/{commit}/{file:path}/{file:name}#L{line:start}-L{line:end}",
                    "{remote:url}/commit/{commit}"
                ),
                URL("https://gitea.io"),
            ),
            TemplatedHost(
                HOST_ID_GOGS,
                "Gogs",
                Icons.GOGS,
                UrlTemplate(
                    "{remote:url}/src/{branch}/{file:path}/{file:name}#L{line:start}-L{line:end}",
                    "{remote:url}/src/{commit}/{file:path}/{file:name}#L{line:start}-L{line:end}",
                    "{remote:url}/commit/{commit}"
                ),
                URL("https://gogs.io")
            ),
            TemplatedHost(
                HOST_ID_AZURE,
                "Azure",
                Icons.AZURE,
                UrlTemplate(
                    "{remote:url}?version=GB{branch}&path=/{file:path}{file:name}&line={line:start}&lineEnd={line:end}&lineStartColumn=1&lineEndColumn=1",
                    "{remote:url}?version=GC{commit}&path=/{file:path}{file:name}&line={line:start}&lineEnd={line:end}&lineStartColumn=1&lineEndColumn=1",
                    "{remote:url}/commit/{commit}"
                ),
                URL("https://dev.azure.com")
            )
        )
    }

    fun provide(): Hosts {
        val settings = project.service<Settings>()

        val customHosts: List<TemplatedHost> = settings.customHosts.map {
            TemplatedHost(
                UUID.fromString(it.id),
                it.displayName,
                Icons.GIT,
                UrlTemplate(it.fileAtBranchTemplate, it.fileAtCommitTemplate, it.commitTemplate),
                URL(it.baseUrl)
            )
        }

        return Hosts(EXISTING.plus(customHosts))
    }
}