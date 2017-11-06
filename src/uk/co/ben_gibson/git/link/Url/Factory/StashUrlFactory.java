package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import java.net.URL;

public class StashUrlFactory extends AbstractUrlFactory
{
    public URL createUrl(CommitDescription description) throws UrlFactoryException, RemoteException
    {
        Remote remote = description.remote();

        String[] parts = this.getParts(remote.url());

        String project    = parts[1];
        String repository = parts[2];

        String path = String.format("/projects/%s/repos/%s/commits/%s", project, repository, description.commitHash());

        return this.buildURL(remote, path, null, null);
    }

    public URL createUrl(FileDescription description) throws UrlFactoryException, RemoteException
    {
        String[] parts = this.getParts(description.remote().url());

        String project    = parts[1];
        String repository = parts[2];

        String path     = String.format("/projects/%s/repos/%s/browse/%s", project, repository, description.file().pathWithName());
        String query    = String.format("at=refs/heads/%s", description.branch());
        String fragment = null;

        if (description.hasLineNumber()) {
            fragment = description.lineNumber().toString();
        }

        return this.buildURL(description.remote(), path, query, fragment);
    }

    public boolean supports(RemoteHost host)
    {
        return host.stash();
    }

    private String[] getParts(URL url) throws UrlFactoryException
    {
        String[] parts = url.getPath().split("/", 3);

        if (parts.length < 3) {
            throw UrlFactoryException.cannotCreateUrl(
                String.format("Could not determine Stash project or repository from URL '%s'", url)
            );
        }

        return parts;
    }
}
