package uk.co.ben_gibson.open.in.git.host.Git;

import com.intellij.openapi.util.IconLoader;
import javax.swing.*;

/**
 * Represents a remote git createHostUrl (GitHub, BitBucket, GitLab etc).
 */
public enum RemoteHost
{
    STASH("Stash", "/Icons/Bitbucket/Bitbucket.png"),
    GIT_HUB("GitHub", "/Icons/GitHub/GitHub.png"),
    BITBUCKET("Bitbucket", "/Icons/Bitbucket/Bitbucket.png"),
    GITLAB("GitLab", "/Icons/GitLab/GitLab.png"),
    GITBLIT("GitBlit", "/Icons/GitHub/GitHub.png");

    private final String name;
    private final String icon;

    RemoteHost(String name, String icon)
    {
        this.name = name;
        this.icon = icon;
    }

    public String toString()
    {
        return this.name;
    }

    public boolean gitHub()
    {
        return (this == GIT_HUB);
    }

    public boolean bitbucket()
    {
        return (this == BITBUCKET);
    }

    public Icon icon()
    {
        return IconLoader.getIcon(this.icon);
    }
}
