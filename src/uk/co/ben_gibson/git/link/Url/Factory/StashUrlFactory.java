package uk.co.ben_gibson.git.link.Url.Factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;

import java.net.URL;

public class StashUrlFactory extends AbstractUrlFactory
{
    public URL createUrlToCommit(@NotNull Remote remote, @NotNull Commit commit) throws UrlFactoryException, RemoteException
    {
        String[] parts = this.getParts(remote.url());

        String project = parts[1];
        String repository = parts[2];

        String path = String.format("/projects/%s/repos/%s/commits/%s", project, repository, commit.hash());

        return this.buildURL(remote, path, null, null);
    }


    public URL createUrlToFileOnBranch(
        @NotNull Remote remote,
        @NotNull File file,
        @NotNull Branch branch,
        @Nullable Integer lineNumber
    ) throws UrlFactoryException, RemoteException
    {
        String[] parts = this.getParts(remote.url());

        String project = parts[1];
        String repository = parts[2];

        String path = String.format("/projects/%s/repos/%s/browse/%s", project, repository, file.path());
        String query = String.format("at=refs/heads/%s", branch.toString());
        String fragment = null;

        if (lineNumber != null) {
            fragment = lineNumber.toString();
        }

        return this.buildURL(remote, path, query, fragment);
    }


    @Override
    public URL createUrlToFileAtCommit(@NotNull Remote remote, @NotNull File file, @NotNull Commit commit, @Nullable Integer lineNumber) throws UrlFactoryException, RemoteException
    {
        String[] parts = this.getParts(remote.url());

        String project = parts[1];
        String repository = parts[2];

        String path = String.format("/projects/%s/repos/%s/browse/%s", project, repository, file.path());
        String query = String.format("at=%s", commit.hash());
        String fragment = null;

        if (lineNumber != null) {
            fragment = lineNumber.toString();
        }

        return this.buildURL(remote, path, query, fragment);
    }


    public boolean supports(RemoteHost host)
    {
        return host.isStash();
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


    @Override
    public boolean canOpenFileAtCommit()
    {
        return true;
    }
}
