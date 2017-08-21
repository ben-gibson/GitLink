package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides remote url factories based on the remote host.
 */
public class RemoteUrlFactoryProvider
{
    private List<RemoteUrlFactory> factories = new ArrayList<RemoteUrlFactory>();

    public void registerFactory(RemoteUrlFactory factory)
    {
        this.factories.add(factory);
    }

    public RemoteUrlFactory remoteUrlFactoryForHost(RemoteHost host) throws RemoteUrlFactoryException
    {
        for (RemoteUrlFactory factory : this.factories) {
            if (factory.supports(host)) {
                return factory;
            }
        }

        throw RemoteUrlFactoryException.unsupportedRemoteHost(host);
    }
}
