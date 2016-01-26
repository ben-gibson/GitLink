package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.RepositoryProvider;

/**
 * Provides URL factories for remote repository providers.
 */
public class UrlFactoryProvider
{

    /**
     * Get a url factory for a remote repository provider.
     *
     * @param provider The provider we want a Url factory for.
     *
     * @return UrlFactory
     */
    @NotNull
    public UrlFactory getUrlFactoryForProvider(RepositoryProvider provider) throws UrlFactoryException
    {
        if (provider == RepositoryProvider.GIT_HUB) {
            return new GitHubUrlFactory();
        } else if (provider == RepositoryProvider.STASH) {
            return new StashUrlFactory();
        }

        throw UrlFactoryException.unsupportedProvider(provider);
    }
}
