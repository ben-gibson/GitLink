package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.RepositoryProvider;

/**
 * Provides URL factories for remote repository providers.
 */
public class UrlFactoryProvider
{

    /**
     * Get a url factory for a provider.
     *
     * @param provider The provider we want a Url factory for.
     *
     * @return UrlFactory
     */
    @NotNull
    public UrlFactory getUrlFactoryForProvider(RepositoryProvider provider)
    {
        if (provider == RepositoryProvider.GIT_HUB) {
            return new GitHubUrlFactory();
        } else if (provider == RepositoryProvider.STASH) {
            return new StashUrlFactory();
        }

        throw new UnsupportedOperationException("Cannot get URL factory for unsupported provider " + provider.toString());
    }
}
