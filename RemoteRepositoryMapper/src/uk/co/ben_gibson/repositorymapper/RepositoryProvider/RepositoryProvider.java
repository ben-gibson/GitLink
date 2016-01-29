package uk.co.ben_gibson.repositorymapper.RepositoryProvider;

/**
 * Represents different repository providers that are supported.
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
    private RepositoryProvider(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return this.name;
    }
}
