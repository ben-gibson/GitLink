package uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.RemoteUrl;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory.Exception.UnsupportedRemoteHostException;

/**
 * A factory that creates URLs to a remote git host from a context.
 */
public interface RemoteUrlFactory
{
    /**
     * Creates url to a specific commit on a remote host.
     */
    RemoteUrl createRemoteUrlToCommit(RemoteHost host, Commit commit, int lineNumber) throws UnsupportedRemoteHostException;

    /**
     * Creates url to a specific file on a remote host.
     */
    RemoteUrl createRemoteUrlToFile(RemoteHost host, Commit commit, int lineNumber) throws UnsupportedRemoteHostException;

    /**
     * Does this factory support the given host.
     */
    boolean supports(RemoteHost host);
}
