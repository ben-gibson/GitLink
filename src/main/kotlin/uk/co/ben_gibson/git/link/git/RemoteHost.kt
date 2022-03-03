package uk.co.ben_gibson.git.link.git

import com.intellij.icons.AllIcons
import javax.swing.Icon

enum class RemoteHost(val displayName: String, val icon: Icon) {
    GIT_HUB("GitHub", AllIcons.Vcs.Vendors.Github),
    BITBUCKET_SERVER("Bitbucket Server", AllIcons.Vcs.Vendors.Github),
    BITBUCKET_CLOUD("Bitbucket Cloud", AllIcons.Vcs.Vendors.Github),
    GITLAB("GitLab", AllIcons.Vcs.Vendors.Github),
    GITBLIT("GitBlit", AllIcons.Vcs.Vendors.Github),
    GITEA("Gitea", AllIcons.Vcs.Vendors.Github),
    GOGS("Gogs", AllIcons.Vcs.Vendors.Github),
    CUSTOM("Custom", AllIcons.Vcs.Vendors.Github);
}