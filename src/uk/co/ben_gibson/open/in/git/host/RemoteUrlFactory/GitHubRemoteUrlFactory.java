package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.*;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteCommitDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteFileDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.net.URL;

public class GitHubRemoteUrlFactory extends AbstractRemoteUrlFactory
{
    public URL createUrl(RemoteCommitDescription description, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        Remote remote = description.remote();

        String path = String.format("/%s/commit/%s", this.cleanPath(remote.url().getPath()), description.commitHash());

        return this.buildURL(remote, path, null, null, forceSSL);
    }

    public URL createUrl(RemoteFileDescription description, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        Remote remote = description.remote();

        String path     = String.format("/%s/blob/%s/%s", this.cleanPath(remote.url().getPath()), description.branch(), this.cleanPath(description.file().path()));
        String fragment = null;

        if (description.hasLineNumber()) {
            fragment = String.format("L%d", description.lineNumber());
        }

        return this.buildURL(remote, path, null, fragment, forceSSL);
    }

    public boolean supports(RemoteHost host)
    {
        return host.gitHub() || host.gitLab();
    }
}
