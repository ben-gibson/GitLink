package uk.co.ben_gibson.git.link.Url.Factory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

abstract class AbstractUrlFactory implements UrlFactory
{
    URL buildURL(@NotNull final Remote remote, @NotNull final String path, final String fragment) throws UrlFactoryException, RemoteException
    {
        URL host = remote.url();

        try {

            URI uri = new URI(host.getProtocol(), host.getHost(), path, null, fragment);

            return uri.toURL();

        } catch (URISyntaxException | MalformedURLException e) {
            throw UrlFactoryException.cannotCreateUrl(e.getMessage());
        }
    }
}
