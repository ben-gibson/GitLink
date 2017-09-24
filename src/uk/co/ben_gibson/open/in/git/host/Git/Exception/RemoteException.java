package uk.co.ben_gibson.open.in.git.host.Git.Exception;

import uk.co.ben_gibson.open.in.git.host.Git.Remote;
import uk.co.ben_gibson.open.in.git.host.Exception.OpenInGitHostException;

/**
 * Thrown when a remote could not be found.
 */
public class RemoteException extends OpenInGitHostException
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
