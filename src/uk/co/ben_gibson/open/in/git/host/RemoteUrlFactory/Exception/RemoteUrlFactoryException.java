package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception;

import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.OpenInGitHostException;

public class RemoteUrlFactoryException extends OpenInGitHostException
{
    private RemoteUrlFactoryException(String message)
    {
        super(message);
    }

    public static RemoteUrlFactoryException unsupportedRemoteHost(RemoteHost host)
    {
        return new RemoteUrlFactoryException(String.format("The remote host '%s' is not supported", host.name()));
    }

    public static RemoteUrlFactoryException cannotCreateRemoteUrl(String reason)
    {
        return new RemoteUrlFactoryException(String.format("Cannot create remote url (%s)", reason));
    }
}
