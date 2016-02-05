package uk.co.ben_gibson.repositorymapper.UrlFactory.Exception;

import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.RepositoryProvider;

/**
 * Thrown when an unsupported provider is given.
 */
public class UnsupportedProviderException extends RemoteRepositoryMapperException
{

    /**
     * Constructor.
     *
     * @param provider The unsupported repository provider.
     */
    public UnsupportedProviderException(RepositoryProvider provider)
    {
        super("Unsupported provider " + provider.toString());
    }
}
