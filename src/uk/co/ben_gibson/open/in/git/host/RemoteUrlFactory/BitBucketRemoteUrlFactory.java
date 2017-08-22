package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Branch;
import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.Git.Remote;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.net.URL;

public class BitBucketRemoteUrlFactory extends AbstractRemoteUrlFactory
{
    public URL createUrlToRemoteCommit(Remote remote, Commit commit, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        String path = String.format("commits/%s", commit.hash());

        return this.buildURL(remote, path, null, null, forceSSL);
    }

    public URL createUrlToRemotePath(Remote remote, Branch branch, String filePath, Integer lineNumber, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException
    {
        String path     = String.format("blob/%s/%s", branch, filePath);
        String query    = String.format("at=%s", branch);
        String fragment = null;

        if (lineNumber != null) {
            fragment = String.format("%s-%s", "s", lineNumber);
        }

        return this.buildURL(remote, path, query, fragment, forceSSL);
    }

    public boolean supports(RemoteHost host)
    {
        return host.bitbucket();
    }
}
