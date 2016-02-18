package uk.co.ben_gibson.repositorymapper.RepositoryProvider;

/**
 * Represents different remote repository providers that are supported.
 */
public enum RepositoryProvider
{
    STASH("Stash"),
    GIT_HUB("GitHub"),
    BITBUCKET("Bitbucket"),
    GITLAB("GitLab");

    private final String name;


    /**
     * Constructor.
     *
     * @param name The repository provider name.
     */
    RepositoryProvider(String name)
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
