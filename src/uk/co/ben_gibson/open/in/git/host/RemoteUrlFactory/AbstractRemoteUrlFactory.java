package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.Git.Remote;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

abstract class AbstractRemoteUrlFactory implements RemoteUrlFactory
{
    URL buildURL(Remote remote, String path, String query, String fragment, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        URL host = remote.url();

        String protocol = (forceSSL) ? "https" : host.getProtocol();

        try {

           URI uri = new URI(protocol, host.getHost(), path, query, fragment);

           return uri.toURL();

        } catch (URISyntaxException | MalformedURLException e) {
            throw RemoteUrlFactoryException.cannotCreateRemoteUrl(e.getMessage());
        }
    }

    String cleanPath(String path)
    {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        if (path.endsWith("/")) {
            path = path.substring(0, (path.length() - 1));
        }

        return path;
    }
}
