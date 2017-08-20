package uk.co.ben_gibson.open.in.git.host;

/**
 * A base exception from which all exceptions from this library should extend!
 */
public abstract class OpenInGitHostException extends Exception
{
    public OpenInGitHostException(String message)
    {
        super(message);
    }
}
