package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.RepositoryProvider;

/**
 * URL factory exception.
 */
public class UrlFactoryException extends Exception
{

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public UrlFactoryException(String message) {
        super(message);
    }


    /**
     * Unsupported remote repository provider.
     *
     * @param provider The unsupported provider.
     *
     * @return UrlFactoryException
     */
    public static UrlFactoryException unsupportedProvider(@NotNull RepositoryProvider provider)
    {
        return new UrlFactoryException("Unsupported remote repository provider " + provider.toString());
    }
}
