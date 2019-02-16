package uk.co.ben_gibson.git.link.Url.Factory.Exception;

import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import uk.co.ben_gibson.git.link.Git.RemoteHost;

public class UrlFactoryException extends GitLinkException
{
    private UrlFactoryException(String message)
    {
        super(message);
    }


    public static UrlFactoryException unsupportedRemoteHost(RemoteHost host)
    {
        return new UrlFactoryException(String.format("The remote host '%s' is not supported", host.name()));
    }


    public static UrlFactoryException cannotCreateUrl(String reason)
    {
        return new UrlFactoryException(String.format("Cannot create url (%s)", reason));
    }
}
