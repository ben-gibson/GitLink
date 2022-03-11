package uk.co.ben_gibson.git.link.git

import com.intellij.icons.AllIcons
import git4idea.repo.GitRemote
import java.net.URL
import javax.swing.Icon

enum class RemoteHost(val displayName: String, val icon: Icon, val defaultUrl: URL) {
    GIT_HUB("GitHub", AllIcons.Vcs.Vendors.Github, URL("https://github.com")),
    BITBUCKET_SERVER("Bitbucket Server", AllIcons.Vcs.Vendors.Github, URL("https://github.com")),
    BITBUCKET_CLOUD("Bitbucket Cloud", AllIcons.Vcs.Vendors.Github, URL("https://github.com")),
    GITLAB("GitLab", AllIcons.Vcs.Vendors.Github, URL("https://gitlab.com")),
    GITBLIT("GitBlit", AllIcons.Vcs.Vendors.Github, URL("https://github.com")),
    GITEA("Gitea", AllIcons.Vcs.Vendors.Github, URL("https://github.com")),
    GOGS("Gogs", AllIcons.Vcs.Vendors.Github, URL("https://github.com")),
    AZURE("Azure", AllIcons.Vcs.Vendors.Github, URL("https://github.com")),
    CUSTOM("Custom", AllIcons.Vcs.Vendors.Github, URL("https://github.com"));

    companion object {
        fun findHostByRemote(remote: GitRemote) = remote
            .httpUrl()
            ?.host
            ?.let { host -> values().first { it.defaultUrl.host == host } }
    }
}