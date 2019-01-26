package uk.co.ben_gibson.git.link.Url.Factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;

import java.net.URL;

public class GitHubUrlFactory extends AbstractUrlFactory
{
    public URL createUrlToCommit(@NotNull Remote remote, @NotNull Commit commit) throws UrlFactoryException, RemoteException
    {
        String path = String.format("/%s/commit/%s", this.cleanPath(remote.url().getPath()), commit.hash());

        return this.buildURL(remote, path, null, null);
    }


    public URL createUrlToFileOnBranch(
        @NotNull Remote remote,
        @NotNull File file,
        @NotNull Branch branch,
        @Nullable LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException
    {

        String path = String.format("/%s/blob/%s/%s", this.cleanPath(remote.url().getPath()), branch.toString(), this.cleanPath(file.path()));
        String fragment = null;

        if (lineSelection != null) {
            fragment = this.formatLineSelection(lineSelection);
        }

        return this.buildURL(remote, path, null, fragment);
    }


    @Override
    public URL createUrlToFileAtCommit(
        @NotNull Remote remote,
        @NotNull File file,
        @NotNull Commit commit,
        @Nullable LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException
    {
        String path = String.format("/%s/blob/%s/%s", this.cleanPath(remote.url().getPath()), commit.hash(), this.cleanPath(file.path()));
        String fragment = null;

        if (lineSelection != null) {
            fragment = this.formatLineSelection(lineSelection);
        }

        return this.buildURL(remote, path, null, fragment);
    }


    public boolean supports(RemoteHost host)
    {
        return host.isGitHub() || host.isGitLab();
    }


    private String formatLineSelection(LineSelection lineSelection)
    {
        if (lineSelection.isMultiLineSelection()) {
            return String.format("L%d-L%d", lineSelection.start(), lineSelection.end());
        }

        return String.format("L%d", lineSelection.start());
    }
}
