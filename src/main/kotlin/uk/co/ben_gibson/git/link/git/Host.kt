package uk.co.ben_gibson.git.link.git

import com.intellij.icons.AllIcons
import git4idea.repo.GitRemote
import uk.co.ben_gibson.git.link.ui.Icons
import java.net.URL
import javax.swing.Icon

enum class Host(val displayName: String, val icon: Icon, val defaultUrl: URL? = null) {
    GIT_HUB("GitHub", AllIcons.Vcs.Vendors.Github, URL("https://github.com")),
    GITLAB("GitLab", Icons.GITLAB, URL("https://gitlab.com")),
    BITBUCKET_SERVER("Bitbucket Server", Icons.BITBUCKET),
    BITBUCKET_CLOUD("Bitbucket Cloud", Icons.BITBUCKET, URL("https://bitbucket.org")),
    GITEA("Gitea", AllIcons.Vcs.Vendors.Github, URL("https://gitea.io")),
    GOGS("Gogs", AllIcons.Vcs.Vendors.Github, URL("https://gogs.io")),
    AZURE("Azure", AllIcons.Vcs.Vendors.Github, URL("https://azure.microsoft.com")),
    CUSTOM("Custom", AllIcons.Vcs.Vendors.Github);

    companion object {
        fun forRemote(remote: GitRemote) = remote
            .httpUrl()
            ?.host
            ?.let { host -> values().first { it.defaultUrl?.host == host } }
    }
}