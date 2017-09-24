package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.*;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteCommitDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteFileDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.net.URL;

public class BitBucketRemoteUrlFactory extends AbstractRemoteUrlFactory
{
    public URL createUrl(RemoteCommitDescription description, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        Remote remote = description.remote();

        String path = String.format("/%s/commits/%s", this.cleanPath(remote.url().getPath()), description.commitHash());

        return this.buildURL(remote, path, null, null, forceSSL);
    }

    public URL createUrl(RemoteFileDescription description, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        Remote remote   = description.remote();
        File file       = description.file();
        String path     = String.format("/%s/src/HEAD/%s", this.cleanPath(remote.url().getPath()), this.cleanPath(file.path()));
        String query    = String.format("at=%s", description.branch());
        String fragment = null;

        if (description.hasLineNumber()) {
            fragment = String.format("%s-%s", file.name(), description.lineNumber());
        }

        return this.buildURL(remote, path, query, fragment, forceSSL);
    }

    public boolean supports(RemoteHost host)
    {
        return host.bitbucket();
    }
}
