package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.Git.File;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;

import java.net.URL;

public interface RemoteUrlFactory
{
    URL createRemoteUrlToCommit(Repository repository, Commit commit, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException;
    URL createRemoteUrlToFile(Repository repository, File file, Integer lineNumber, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException;
    boolean supports(RemoteHost host);
}
