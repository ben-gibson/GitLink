package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a URL factory for a given remote git host.
 */
public class UrlFactoryProvider
{
    private List<UrlFactory> factories = new ArrayList<UrlFactory>();

    public void registerFactory(UrlFactory factory)
    {
        this.factories.add(factory);
    }

    public UrlFactory urlFactory(RemoteHost host) throws UrlFactoryException
    {
        for (UrlFactory factory : this.factories) {
            if (factory.supports(host)) {
                return factory;
            }
        }

        throw UrlFactoryException.unsupportedRemoteHost(host);
    }
}
