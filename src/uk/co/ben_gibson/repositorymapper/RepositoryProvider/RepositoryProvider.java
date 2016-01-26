package uk.co.ben_gibson.repositorymapper.RepositoryProvider;

/**
 * Represents different remote repository providers that we support.
 */
public enum RepositoryProvider
{
    STASH("Stash"),
    GIT_HUB("GitHub");

    private final String name;


    /**
     * Constructor.
     *
     * @param name The name.
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
