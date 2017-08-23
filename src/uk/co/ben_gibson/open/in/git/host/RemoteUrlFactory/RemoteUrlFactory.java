package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.*;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;

import java.net.URL;

public interface RemoteUrlFactory
{
    URL createUrlToCommit(Remote remote, Commit commit, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException;
    URL createUrlToFile(Remote remote, Branch branch, File file, Integer lineNumber, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException;
    boolean supports(RemoteHost host);
}
