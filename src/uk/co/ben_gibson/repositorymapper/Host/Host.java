package uk.co.ben_gibson.repositorymapper.Host;

/**
 * Represents a git host.
 */
public enum Host
{
    STASH("Stash"),
    GIT_HUB("GitHub"),
    BITBUCKET("Bitbucket"),
    GITLAB("GitLab");

    private final String name;

    /**
     * Constructor.
     *
     * @param name The host name.
     */
    Host(String name)
    {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return this.name;
    }
}
