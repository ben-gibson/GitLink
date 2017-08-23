package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.*;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.net.URL;

public class GitHubRemoteUrlFactory extends AbstractRemoteUrlFactory
{
    public URL createUrlToCommit(Remote remote, Commit commit, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        String path = String.format("/%s/commit/%s", this.cleanPath(remote.url().getPath()), commit.hash());

        return this.buildURL(remote, path, null, null, forceSSL);
    }

    public URL createUrlToFile(Remote remote, Branch branch, File file, Integer lineNumber, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        String path     = String.format("/%s/blob/%s/%s", this.cleanPath(remote.url().getPath()), branch, this.cleanPath(file.path()));
        String fragment = null;

        if (lineNumber != null) {
            fragment = String.format("L%d", lineNumber);
        }

        return this.buildURL(remote, path, null, fragment, forceSSL);
    }

    public boolean supports(RemoteHost host)
    {
        return host.gitHub() || host.gitLab();
    }
}
