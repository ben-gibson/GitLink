package uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.RemoteUrl;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory.Exception.UnsupportedRemoteHostException;

/**
 * Creates remote git urls to Github.
 */
public class GitHubRemoteUrlFactory implements RemoteUrlFactory
{
    private boolean forceSSL;

    /**
     * Constrictor
     */
    public GitHubRemoteUrlFactory(boolean forceSSL)
    {
        this.forceSSL = forceSSL;
    }

    @Override
    public RemoteUrl createRemoteUrlToCommit(RemoteHost host, Commit commit, int lineNumber) throws UnsupportedRemoteHostException
    {
        if (!this.supports(host)) {
            throw new UnsupportedRemoteHostException(host);
        }

        return null;
    }

    @Override
    public RemoteUrl createRemoteUrlToFile(RemoteHost host, Commit commit, int lineNumber) throws UnsupportedRemoteHostException
    {
        if (!this.supports(host)) {
            throw new UnsupportedRemoteHostException(host);
        }

        return null;
    }

    @Override
    public boolean supports(RemoteHost host)
    {
        return host.isGitHub();
    }
}
