package uk.co.ben_gibson.repositorymapper.Repository.Exception;

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
}