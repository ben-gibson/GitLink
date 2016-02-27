package uk.co.ben_gibson.repositorymapper.Repository.Exception;

/**
 * Thrown when fetching remote branches failed.
 */
public class CouldNotFetchRemoteBranchesException extends RemoteNotFoundException
{

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public CouldNotFetchRemoteBranchesException(String message)
    {
        super(message);
    }
}
