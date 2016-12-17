package uk.co.ben_gibson.repositorymapper.Host;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;

/**
 * Represents a git host.
 */
public enum Host
{
    STASH("Stash", "/icons/Bitbucket/Bitbucket.png"),
    GIT_HUB("GitHub", "/icons/GitHub/GitHub.png"),
    BITBUCKET("Bitbucket", "/icons/Bitbucket/Bitbucket.png"),
    GITLAB("GitLab", "/icons/GitLab/GitLab.png"),
    GITBLIT("GitBlit", "/icons/GitHub/GitHub.png");

    private final String name;
    private final String icon;

    /**
     * Constructor.
     *
     * @param name The host name.
     * @param icon The host icon.
     */
    Host(@NotNull String name, @NotNull String icon)
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
     * Get icon.
     *
     * @return Icon
     */
    public Icon getIcon()
    {
        return IconLoader.getIcon(this.icon);
    }
}
