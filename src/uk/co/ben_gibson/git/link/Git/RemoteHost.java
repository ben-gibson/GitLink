package uk.co.ben_gibson.git.link.Git;

import com.intellij.openapi.util.IconLoader;
import javax.swing.*;

/**
 * Represents a remote git createHostUrl (GitHub, BitBucket, GitLab etc).
 */
public enum RemoteHost
{
    GIT_HUB("GitHub", "/Icons/GitHub/GitHub.png"),
    STASH("Stash", "/Icons/Bitbucket/Bitbucket.png"),
    BITBUCKET("Bitbucket", "/Icons/Bitbucket/Bitbucket.png"),
    GITLAB("GitLab", "/Icons/GitLab/GitLab.png"),
    CUSTOM("Custom", "/Icons/Custom/Custom.png");

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

    public boolean custom()
    {
        return (this == CUSTOM);
    }

    public boolean gitHub()
    {
        return (this == GIT_HUB);
    }

    public boolean gitLab()
    {
        return (this == GITLAB);
    }

    public boolean bitbucket()
    {
        return (this == BITBUCKET);
    }

    public boolean stash()
    {
        return (this == STASH);
    }

    public Icon icon()
    {
        return IconLoader.getIcon(this.icon);
    }
}
