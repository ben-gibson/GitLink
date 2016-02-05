package uk.co.ben_gibson.repositorymapper;

/**
 * A base exception for the project from which all other exceptions should extend.
 */
public class RemoteRepositoryMapperException extends Exception
{

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public RemoteRepositoryMapperException(String message)
    {
        super(message);
    }
}
