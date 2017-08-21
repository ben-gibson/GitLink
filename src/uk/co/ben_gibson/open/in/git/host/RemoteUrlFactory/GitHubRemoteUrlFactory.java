package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.Git.File;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Creates remote git urls to Github.
 */
public class GitHubRemoteUrlFactory implements RemoteUrlFactory
{
    public URL createRemoteUrlToCommit(Repository repository, Commit commit, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        URL host = repository.origin().url();

        String path = String.format("%s/commit/%s", host.getPath(), commit.hash());

        String protocol = (forceSSL) ? "https" : host.getProtocol();

        try {
            return new URI(protocol, host.getHost(), path, null).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw RemoteUrlFactoryException.cannotCreateRemoteUrl(e.getMessage());
        }
    }

    public URL createRemoteUrlToFile(Repository repository, File file, Integer lineNumber, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        URL host = repository.origin().url();
        String fragment = null;

        String path = String.format(
            "%s/blob/%s%s",
            host.getPath(),
            repository.currentBranch(),
            repository.getRelativePath(file)
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
