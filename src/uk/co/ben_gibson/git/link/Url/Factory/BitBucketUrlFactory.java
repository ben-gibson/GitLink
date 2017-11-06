package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import java.net.URL;

public class BitBucketUrlFactory extends AbstractUrlFactory
{
    public URL createUrl(CommitDescription description) throws UrlFactoryException, RemoteException
    {
        Remote remote = description.remote();

        String path = String.format("/%s/commits/%s", this.cleanPath(remote.url().getPath()), description.commitHash());

        return this.buildURL(remote, path, null, null);
    }

    public URL createUrl(FileDescription description) throws UrlFactoryException, RemoteException
    {
        Remote remote   = description.remote();
        File file       = description.file();
        String path     = String.format("/%s/src/HEAD/%s", this.cleanPath(remote.url().getPath()), this.cleanPath(file.pathWithName()));
        String query    = String.format("at=%s", description.branch());
        String fragment = null;

        if (description.hasLineNumber()) {
            fragment = String.format("%s-%s", file.name(), description.lineNumber());
        }

        return this.buildURL(remote, path, query, fragment);
    }

    public boolean supports(RemoteHost host)
    {
        return host.bitbucket();
    }
}
