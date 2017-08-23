package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.*;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteCommitDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteFileDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;

import java.net.URL;

public interface RemoteUrlFactory
{
    URL createUrl(RemoteCommitDescription description, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException;
    URL createUrl(RemoteFileDescription description, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException;
    boolean supports(RemoteHost host);
}
