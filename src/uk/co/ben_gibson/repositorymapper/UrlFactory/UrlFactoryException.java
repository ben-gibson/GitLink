package uk.co.ben_gibson.repositorymapper.UrlFactory;

import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.RepositoryProvider;

import java.net.URL;

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
    public UrlFactoryException(String message)
    {
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


    /**
     * Could not find a remote branch.
     *
     * @return UrlFactoryException
     */
    public static UrlFactoryException remoteBranchNotFound()
    {
        return new UrlFactoryException("Could not find a remote branch");
    }



    /**
     * Origin remote not found for repository.
     *
     * @param repository The repository that has no origin remote.
     *
     * @return UrlFactoryException
     */
    public static UrlFactoryException originRemoteNotFound(@NotNull GitRepository repository)
    {
        return new UrlFactoryException("The origin remote was not found for repository at path " + repository.getRoot().getPath());
    }


    /**
     * No url found on remote.
     *
     * @param remote The remote with no URL.
     *
     * @return UrlFactoryException
     */
    public static UrlFactoryException urlNotFoundForRemote(@NotNull GitRemote remote)
    {
        return new UrlFactoryException("URL not found on remote " + remote.getName());
    }


    /**
     * Project and repository name could not be found in url.
     *
     * @param url The URL which did not contain a valid project or repository name.
     *
     * @return UrlFactoryException
     */
    public static UrlFactoryException projectAndRepoNameNotFoundInUrl(@NotNull URL url)
    {
        return new UrlFactoryException("Could not find project and repo in Url " + url.toString());
    }
}
