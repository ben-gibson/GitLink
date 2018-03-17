package uk.co.ben_gibson.git.link.Git.Exception;

import uk.co.ben_gibson.git.link.Exception.Codes;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;

/**
 * Thrown when a remote could not be found.
 */
public class RemoteException extends GitLinkException
{
    private RemoteException(String message, int code)
    {
        super(message, code);
    }

    public static RemoteException remoteNotFound(String remoteName)
    {
        return new RemoteException(
            String.format("Could not find the remote '%s'", remoteName),
            Codes.GIT_COULD_NOT_FIND_REMOTE
        );
    }

    public static RemoteException urlNotFoundForRemote(Remote remote)
    {
        return new RemoteException(
            String.format("Could not determine the url associated with remote '%s'", remote.name()),
            Codes.GIT_COULD_NOT_FIND_URL_FOR_REMOTE
        );
    }
}
