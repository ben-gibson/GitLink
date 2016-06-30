package uk.co.ben_gibson.repositorymapper.Context.Exception;

/**
 * Thrown when in an invalid context.
 */
public class InvalidContextException extends Exception
{
    /**
     * Constructor.
     *
     * @param message The error message.
     */
    public InvalidContextException(String message)
    {
        super(message);
    }
}
