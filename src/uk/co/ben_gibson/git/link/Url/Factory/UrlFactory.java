package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import java.net.URL;

public interface UrlFactory
{
    URL createUrl(CommitDescription description) throws UrlFactoryException, RemoteException;
    URL createUrl(FileDescription description) throws UrlFactoryException, RemoteException;
    boolean supports(RemoteHost host);
}
