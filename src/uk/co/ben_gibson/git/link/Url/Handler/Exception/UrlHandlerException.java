package uk.co.ben_gibson.git.link.Url.Handler.Exception;

import uk.co.ben_gibson.git.link.Exception.GitLinkException;

/**
 * Thrown when a handler cannot handle the URL.
 */
public class UrlHandlerException extends GitLinkException
{
    public UrlHandlerException(String message)
    {
        super(message);
    }
}
