package uk.co.ben_gibson.git.link.Url.Factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import java.net.URL;

public interface UrlFactory
{
    URL createUrlToCommit(@NotNull final Remote remote, @NotNull final Commit commit) throws UrlFactoryException, RemoteException;

    URL createUrlToFileOnBranch(
        @NotNull final Remote remote,
        @NotNull final File file,
        @NotNull final Branch branch,
        @Nullable final LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException;

    URL createUrlToFileAtCommit(
        @NotNull final Remote remote,
        @NotNull final File file,
        @NotNull final Commit commit,
        @Nullable final LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException;

    boolean supports(@NotNull final RemoteHost host);
}
