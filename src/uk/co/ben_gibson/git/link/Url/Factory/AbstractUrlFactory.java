package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

abstract class AbstractUrlFactory implements UrlFactory
{
    URL buildURL(Remote remote, String path, String query, String fragment) throws UrlFactoryException, RemoteException
    {
        URL host = remote.url();

        try {

           URI uri = new URI(host.getProtocol(), host.getHost(), path, query, fragment);

           return uri.toURL();

        } catch (URISyntaxException | MalformedURLException e) {
            throw UrlFactoryException.cannotCreateUrl(e.getMessage());
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
