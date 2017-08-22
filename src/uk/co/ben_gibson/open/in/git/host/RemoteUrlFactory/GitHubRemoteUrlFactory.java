package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.*;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Creates URLs to Github.
 */
public class GitHubRemoteUrlFactory implements RemoteUrlFactory
{
    public URL createUrlToRemoteCommit(Remote remote, Commit commit, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        URL host = remote.url();

        String path = String.format("%s/commit/%s", host.getPath(), commit.hash());

        String protocol = (forceSSL) ? "https" : host.getProtocol();

        try {
            return new URI(protocol, host.getHost(), path, null).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw RemoteUrlFactoryException.cannotCreateRemoteUrl(e.getMessage());
        }
    }

    public URL createUrlToRemotePath(Remote remote, Branch branch, String filePath, Integer lineNumber, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        URL host = remote.url();

        String fragment = null;

        String path = String.format(
            "%s/blob/%s%s",
            host.getPath(),
            branch,
            filePath
        );

        if (lineNumber != null) {
            fragment = String.format("L%d", lineNumber);
        }

        try {
            return new URI(host.getProtocol(), host.getHost(), path, fragment).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw RemoteUrlFactoryException.cannotCreateRemoteUrl(e.getMessage());
        }
    }

    public boolean supports(RemoteHost host)
    {
        return host.gitHub();
    }
}
