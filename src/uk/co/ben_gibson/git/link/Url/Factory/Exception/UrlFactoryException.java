package uk.co.ben_gibson.git.link.Url.Factory.Exception;

import uk.co.ben_gibson.git.link.Exception.Codes;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import uk.co.ben_gibson.git.link.Git.RemoteHost;

public class UrlFactoryException extends GitLinkException
{
    private UrlFactoryException(String message, int code)
    {
        super(message, code);
    }


    public static UrlFactoryException unsupportedRemoteHost(RemoteHost host)
    {
        return new UrlFactoryException(
            String.format("The remote host '%s' is not supported", host.name()),
            Codes.URL_FACTORY_UNSUPPORTED_REMOTE_HOST
        );
    }


    public static UrlFactoryException cannotCreateUrl(String reason)
    {
        return new UrlFactoryException(
            String.format("Cannot create url (%s)", reason),
            Codes.URL_FACTORY_CANNOT_CREATE_URL
        );
    }
}
