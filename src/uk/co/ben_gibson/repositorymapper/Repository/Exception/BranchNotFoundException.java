package uk.co.ben_gibson.repositorymapper.Repository.Exception;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;

/**
 * Thrown when a branch could not be found.
 */
public class BranchNotFoundException extends RemoteRepositoryMapperException
{


    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public BranchNotFoundException(String message)
    {
        super(message);
    }


    /**
     * Could not find active branch that tracked a remote branch.
     *
     * @return BranchNotFoundException
     */
    @NotNull
    public static BranchNotFoundException activeBranchWithRemoteTrackingNotFound()
    {
        return new BranchNotFoundException("Could not find an active branch that tracked a remote branch");
    }
}