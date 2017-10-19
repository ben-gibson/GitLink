package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import java.net.URL;

public class GitHubUrlFactory extends AbstractUrlFactory
{
    public URL createUrl(CommitDescription description) throws UrlFactoryException, RemoteException
    {
        Remote remote = description.remote();

        String path = String.format("/%s/commit/%s", this.cleanPath(remote.url().getPath()), description.commitHash());

        return this.buildURL(remote, path, null, null);
    }

    public URL createUrl(FileDescription description) throws UrlFactoryException, RemoteException
    {
        Remote remote = description.remote();

        String path     = String.format("/%s/blob/%s/%s", this.cleanPath(remote.url().getPath()), description.branch(), this.cleanPath(description.file().pathWithName()));
        String fragment = null;

        if (description.hasLineNumber()) {
            fragment = String.format("L%d", description.lineNumber());
        }

        return this.buildURL(remote, path, null, fragment);
    }

    public boolean supports(RemoteHost host)
    {
        return host.gitHub() || host.gitLab();
    }
}
