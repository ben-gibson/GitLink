package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.*;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.net.URL;

public class StashRemoteUrlFactory extends AbstractRemoteUrlFactory
{
    public URL createUrlToCommit(Remote remote, Commit commit, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        String[] parts = this.detailsFromRemoteUrl(remote.url());

        String project    = parts[1];
        String repository = parts[2];

        String path = String.format("/projects/%s/repos/%s/commits/%s", project, repository, commit.hash());

        return this.buildURL(remote, path, null, null, forceSSL);
    }

    public URL createUrlToFile(Remote remote, Branch branch, File file, Integer lineNumber, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        String[] parts = this.detailsFromRemoteUrl(remote.url());

        String project    = parts[1];
        String repository = parts[2];

        String path     = String.format("/projects/%s/repos/%s/browse/%s", project, repository, file.path());
        String query    = String.format("at=refs/heads/%s", branch);
        String fragment = null;

        if (lineNumber != null) {
            fragment = lineNumber.toString();
        }

        return this.buildURL(remote, path, query, fragment, forceSSL);
    }

    public boolean supports(RemoteHost host)
    {
        return host.stash();
    }

    private String[] detailsFromRemoteUrl(URL url) throws RemoteUrlFactoryException
    {
        String[] parts = url.getPath().split("/", 3);

        if (parts.length < 3) {
            throw RemoteUrlFactoryException.cannotCreateRemoteUrl(
                String.format("Could not determine Stash project or repository from remote URL '%s'", url)
            );
        }

        return parts;
    }
}
