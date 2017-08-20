package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.File;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.net.URL;

public interface RemoteUrlFactory
{
    URL createRemoteUrlToCommit(RemoteHost host, Repository repository, Commit commit) throws RemoteUrlFactoryException;
    URL createRemoteUrlToFile(RemoteHost host, Repository repository, File file, Integer lineNumber) throws RemoteUrlFactoryException;
    boolean supports(RemoteHost host);
}
