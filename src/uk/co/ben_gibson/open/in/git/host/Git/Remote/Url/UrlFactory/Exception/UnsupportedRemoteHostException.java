package uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory.Exception;

import uk.co.ben_gibson.open.in.git.host.Git.Remote.RemoteHost;

/**
 * Thrown when a remote host isn't supported.
 */
public class UnsupportedRemoteHostException extends Exception
{
    /**
     * Constructor.
     *
     * @param host The unsupported host.
     */
    public UnsupportedRemoteHostException(RemoteHost host)
    {
        super("The remote host " + host.toString() + "is not supported");
    }
}
