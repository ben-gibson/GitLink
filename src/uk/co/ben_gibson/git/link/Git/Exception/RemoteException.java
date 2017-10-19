package uk.co.ben_gibson.git.link.Git.Exception;

import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;

/**
 * Thrown when a remote could not be found.
 */
public class RemoteException extends GitLinkException
{
    private RemoteException(String message)
    {
        super(message);
    }

    public static RemoteException originNotFound()
    {
        return new RemoteException("Could not find the remote 'origin'");
    }

    public static RemoteException urlNotFoundForRemote(Remote remote)
    {
        return new RemoteException("Could not determine the url associated with remote " + remote.name());
    }
}
