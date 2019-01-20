package uk.co.ben_gibson.git.link.Git;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public enum RemoteHost
{
    GIT_HUB("GitHub", "/Icons/GitHub/GitHub.png"),
    BITBUCKET_SERVER("Bitbucket Server", "/Icons/Bitbucket/Bitbucket.png"),
    BITBUCKET_CLOUD("Bitbucket Cloud", "/Icons/Bitbucket/Bitbucket.png"),
    GITLAB("GitLab", "/Icons/GitLab/GitLab.png"),
    GITBLIT("GitBlit", ""),
    GITEA("Gitea", "/Icons/Gitea/Gitea.png"),
    GOGS("Gogs", "/Icons/Gogs/Gogs.png"),
    CUSTOM("Custom", "/Icons/Custom/Custom.png");

    private final String displayName;
    private final String icon;

    RemoteHost(@NotNull String displayName, @NotNull String icon)
    {
        this.displayName = displayName;
        this.icon        = icon;
    }

    @NotNull
    public String toString()
    {
        return this.displayName;
    }

    public boolean isCustom()
    {
        return (this == CUSTOM);
    }

    public boolean isGitBlit()
    {
        return (this == GITBLIT);
    }

    public boolean isGitHub()
    {
        return (this == GIT_HUB);
    }

    public boolean isGitLab()
    {
        return (this == GITLAB);
    }

    public boolean isBitbucketCloud()
    {
        return (this == BITBUCKET_CLOUD);
    }

    public boolean isBitbucketServer()
    {
        return (this == BITBUCKET_SERVER);
    }

    public boolean isGitea()
    {
        return (this == GITEA);
    }

    public boolean isGogs()
    {
        return (this == GOGS);
    }

    @NotNull
    public Icon icon()
    {
        return IconLoader.getIcon(this.icon);
    }
}
