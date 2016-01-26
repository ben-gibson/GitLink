package uk.co.ben_gibson.repositorymapper.Context;

import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when when a context cannot be provided.
 */
public class ContextProviderException extends Exception
{

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public ContextProviderException(String message) {
        super(message);
    }


    /**
     * Origin remote not found for repository.
     *
     * @param repository The repository that has no origin remote.
     *
     * @return ContextProviderException
     */
    public static ContextProviderException originRemoteNotFound(@NotNull GitRepository repository)
    {
        return new ContextProviderException("The origin remote was not found for repository at path " + repository.getRoot().getPath());
    }


    /**
     * No url found on remote.
     *
     * @param remote The remote with no URL.
     *
     * @return ContextProviderException
     */
    public static ContextProviderException urlNotFoundForRemote(@NotNull GitRemote remote)
    {
        return new ContextProviderException("URL not found on remote " + remote.getName());
    }
}
