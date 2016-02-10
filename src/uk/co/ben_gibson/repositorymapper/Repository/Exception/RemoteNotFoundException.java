package uk.co.ben_gibson.repositorymapper.Repository.Exception;

import git4idea.repo.GitRemote;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;

/**
 * Thrown when a remote could not be found.
 */
public class RemoteNotFoundException extends RemoteRepositoryMapperException
{

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public RemoteNotFoundException(String message)
    {
        super(message);
    }


    /**
     * Could not find the remote 'origin'
     *
     * @return RemoteNotFoundException
     */
    @NotNull
    public static RemoteNotFoundException remoteOriginNotFound()
    {
        return new RemoteNotFoundException("Could not find the remote 'origin'");
    }


    /**
     * Could not find the url associated with the remote
     *
     * @return RemoteNotFoundException
     */
    @NotNull
    public static RemoteNotFoundException urlNotFoundForRemote(@NotNull GitRemote remote)
    {
        return new RemoteNotFoundException("Could not find the url associated with remote " + remote.getName());
    }
}
