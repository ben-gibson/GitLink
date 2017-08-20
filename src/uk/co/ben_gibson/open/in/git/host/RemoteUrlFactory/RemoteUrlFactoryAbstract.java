package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import com.intellij.openapi.util.text.StringUtil;
import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.Git.File;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

abstract class RemoteUrlFactoryAbstract implements RemoteUrlFactory
{
    private boolean forceSSL;

    protected abstract URL createRemoteUrlToCommit(Repository repository, Commit commit) throws RemoteUrlFactoryException;
    protected abstract URL createRemoteUrlToFile(Repository repository, File file, Integer lineNumber) throws RemoteUrlFactoryException;

    RemoteUrlFactoryAbstract(boolean forceSSL)
    {
        this.forceSSL = forceSSL;
    }

    public URL createRemoteUrlToCommit(RemoteHost host, Repository repository, Commit commit) throws RemoteUrlFactoryException
    {
        if (!this.supports(host)) {
            throw RemoteUrlFactoryException.unsupportedRemoteHost(host);
        }

        return this.createRemoteUrlToCommit(repository, commit);
    }

    public URL createRemoteUrlToFile(RemoteHost host, Repository repository, File file, Integer lineNumber) throws RemoteUrlFactoryException
    {
        if (!this.supports(host)) {
            throw RemoteUrlFactoryException.unsupportedRemoteHost(host);
        }

        return this.createRemoteUrlToFile(repository, file, lineNumber);
    }

    URL createHostUrl(Repository repository) throws RemoteUrlFactoryException
    {
        try {
            String host = StringUtil.trimEnd(repository.originUrl(), ".git");

            host = host.replaceAll(":\\d{1,4}", ""); // remove port

            if (!host.startsWith("http")) {
                host = StringUtil.replace(host, "git@", "");
                host = StringUtil.replace(host, "ssh://", "");
                host = StringUtil.replace(host, "git://", "");
                host = String.format(
                    "%s://%s",
                    (this.forceSSL) ? "https" : "http",
                    StringUtil.replace(host, ":", "/")
                );
            }

            return new URL(host);

        } catch (RemoteException | MalformedURLException e) {
            throw RemoteUrlFactoryException.cannotCreateRemoteUrl(e.getMessage());
        }
    }

    URL createRemoteUrl(URL host, String path, String fragment) throws RemoteUrlFactoryException
    {
        try {
            return new URI(host.getProtocol(), host.getHost(), path, fragment).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw RemoteUrlFactoryException.cannotCreateRemoteUrl(e.getMessage());
        }
    }
}
