package uk.co.ben_gibson.open.in.git.host.Git.Remote;

import com.intellij.openapi.util.IconLoader;
import javax.swing.*;

/**
 * Represents a remote git host (GitHub, BitBucket, GitLab etc).
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

    /**
     * Constructor.
     *
     * @param name The host name.
     * @param icon The host icon.
     */
    RemoteHost(String name, String icon)
    {
        this.name = name;
        this.icon = icon;
    }

    /**
     * Get name.
     *
     * @return String
     */
    public String toString()
    {
        return this.name;
    }

    /**
     * Does this represent GitHub?
     */
    public boolean isGitHub()
    {
        return (this == GIT_HUB);
    }

    /**
     * Get icon.
     *
     * @return Icon
     */
    public Icon getIcon()
    {
        return IconLoader.getIcon(this.icon);
    }
}
