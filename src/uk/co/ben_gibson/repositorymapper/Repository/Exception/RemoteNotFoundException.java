package uk.co.ben_gibson.repositorymapper.Repository.Exception;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;
import uk.co.ben_gibson.repositorymapper.Repository.Remote;

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
     * Could not find 'origin'
     *
     * @return RemoteNotFoundException
     */
    @NotNull
    public static RemoteNotFoundException originNotFound()
    {
        return new RemoteNotFoundException("Could not find the remote 'origin'");
    }


    /**
     * Could not find a url associated with the remote
     *
     * @param remote The remote that had no url.
     *
     * @return RemoteNotFoundException
     */
    @NotNull
    public static RemoteNotFoundException urlNotFoundForRemote(@NotNull Remote remote)
    {
        return new RemoteNotFoundException("Could not find the url associated with remote " + remote.getName());
    }
}
