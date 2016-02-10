package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.RepositoryProvider;
import uk.co.ben_gibson.repositorymapper.UrlFactory.Exception.UnsupportedProviderException;

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
    public UrlFactory getUrlFactoryForProvider(RepositoryProvider provider) throws UnsupportedProviderException
    {
        if (provider == RepositoryProvider.GIT_HUB || provider == RepositoryProvider.GITLAB) {
            return new GitHubUrlFactory();
        } else if (provider == RepositoryProvider.STASH) {
            return new StashUrlFactory();
        }  else if (provider == RepositoryProvider.BITBUCKET) {
            return new BitBucketUrlFactory();
        }

        throw new UnsupportedProviderException(provider);
    }
}
