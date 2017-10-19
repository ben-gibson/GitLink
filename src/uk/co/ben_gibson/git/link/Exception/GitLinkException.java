package uk.co.ben_gibson.git.link.Exception;

/**
 * A base exception from which all exceptions from this plugin should extend!
 */
public abstract class GitLinkException extends Exception
{
    public GitLinkException(String message)
    {
        super(message);
    }
}
