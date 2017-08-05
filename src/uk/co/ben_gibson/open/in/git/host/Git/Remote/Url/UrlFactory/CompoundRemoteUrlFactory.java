package uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.RemoteUrl;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory.Exception.UnsupportedRemoteHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * A compound collection of remote url factories.
 */
public class CompoundRemoteUrlFactory implements RemoteUrlFactory
{
    private List<RemoteUrlFactory> factories = new ArrayList<RemoteUrlFactory>();

    @Override
    public RemoteUrl createRemoteUrlToCommit(RemoteHost host, Commit commit, int lineNumber) throws UnsupportedRemoteHostException
    {
        for (RemoteUrlFactory factory : this.factories) {
            if (factory.supports(host)) {
                return factory.createRemoteUrlToCommit(host, commit, lineNumber);
            }
        }

        throw new UnsupportedRemoteHostException(host);
    }

    @Override
    public RemoteUrl createRemoteUrlToFile(RemoteHost host, Commit commit, int lineNumber) throws UnsupportedRemoteHostException
    {
        for (RemoteUrlFactory factory : this.factories) {
            if (factory.supports(host)) {
                return factory.createRemoteUrlToFile(host, commit, lineNumber);
            }
        }

        throw new UnsupportedRemoteHostException(host);
    }

    @Override
    public boolean supports(RemoteHost host)
    {
        for (RemoteUrlFactory factory : this.factories) {
            if (factory.supports(host)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Register a remote url factory.
     *
     * @param factory The factory to register.
     */
    public void registerFactory(RemoteUrlFactory factory)
    {
        this.factories.add(factory);
    }
}
