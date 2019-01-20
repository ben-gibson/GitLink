package uk.co.ben_gibson.git.link.Url.Factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;

import java.net.URL;

public class BitbucketCloudUrlFactory extends AbstractUrlFactory
{
    public URL createUrlToCommit(@NotNull Remote remote, @NotNull Commit commit) throws UrlFactoryException, RemoteException
    {
        String path = String.format("/%s/commits/%s", this.cleanPath(remote.url().getPath()), commit.hash());

        return this.buildURL(remote, path, null, null);
    }


    public URL createUrlToFileOnBranch(
        @NotNull Remote remote,
        @NotNull File file,
        @NotNull Branch branch,
        @Nullable LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException
    {
        String path = String.format("/%s/src/HEAD/%s", this.cleanPath(remote.url().getPath()), this.cleanPath(file.path()));
        String query = String.format("at=%s", branch.toString());
        String fragment = null;

        if (lineSelection != null) {
            fragment = this.formatLineSelection(lineSelection);
        }

        return this.buildURL(remote, path, query, fragment);
    }


    @Override
    public URL createUrlToFileAtCommit(
        @NotNull Remote remote,
        @NotNull File file,
        @NotNull Commit commit,
        @Nullable LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException
    {
        String path = String.format("/%s/src/%s/%s", this.cleanPath(remote.url().getPath()), commit.hash(), this.cleanPath(file.path()));

        String fragment = null;

        if (lineSelection != null) {
            fragment = this.formatLineSelection(lineSelection);
        }

        return this.buildURL(remote, path, null, fragment);
    }


    public boolean supports(RemoteHost host)
    {
        return host.isBitbucketCloud();
    }


    private String formatLineSelection(LineSelection lineSelection)
    {
        if (lineSelection.isMultiLineSelection()) {
            return String.format("lines-%d:%d", lineSelection.start(), lineSelection.end());
        }

        return String.format("lines-%d", lineSelection.start());
    }
}
