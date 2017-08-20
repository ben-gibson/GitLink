package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.File;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A compound collection of remote url factories.
 */
public class CompoundRemoteUrlFactory implements RemoteUrlFactory
{
    private List<RemoteUrlFactory> factories = new ArrayList<RemoteUrlFactory>();

    public URL createRemoteUrlToCommit(RemoteHost host, Repository repository, Commit commit) throws RemoteUrlFactoryException
    {
        return this.remoteUrlFactoryForHost(host).createRemoteUrlToCommit(host, repository, commit);
    }

    public URL createRemoteUrlToFile(RemoteHost host, Repository repository, File file, Integer lineNumber) throws RemoteUrlFactoryException
    {
        return this.remoteUrlFactoryForHost(host).createRemoteUrlToFile(host, repository, file, lineNumber);
    }

    public boolean supports(RemoteHost host)
    {
        for (RemoteUrlFactory factory : this.factories) {
            if (factory.supports(host)) {
                return true;
            }
        }

        return false;
    }

    public void registerFactory(RemoteUrlFactoryAbstract factory)
    {
        this.factories.add(factory);
    }

    private RemoteUrlFactory remoteUrlFactoryForHost(RemoteHost host) throws RemoteUrlFactoryException
    {
        for (RemoteUrlFactory factory : this.factories) {
            if (factory.supports(host)) {
                return factory;
            }
        }

        throw RemoteUrlFactoryException.unsupportedRemoteHost(host);
    }
}
