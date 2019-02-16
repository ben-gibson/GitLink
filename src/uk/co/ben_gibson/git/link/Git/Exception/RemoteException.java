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

    public static RemoteException remoteNotFound(String remoteName)
    {
        return new RemoteException(String.format("Could not find the remote '%s' in the repository", remoteName));
    }

    public static RemoteException urlNotFoundForRemote(Remote remote)
    {
        return new RemoteException(String.format("Could not determine the URL for the remote '%s'", remote.name()));
    }
}
