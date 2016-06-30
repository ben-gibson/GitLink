package uk.co.ben_gibson.repositorymapper.Repository.Exception;

/**
 * Thrown when a branch could not be found.
 */
public class BranchNotFoundException extends Exception {

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