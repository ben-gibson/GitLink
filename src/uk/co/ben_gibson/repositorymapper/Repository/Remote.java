package uk.co.ben_gibson.repositorymapper.Repository;

import git4idea.repo.GitRemote;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;

/**
 * Decorates a git remote.
 */
public class Remote
{

    @NotNull
    private GitRemote remote;

    /**
     * Constructor.
     *
     * @param remote The git remote to decorate
     */
    public Remote(@NotNull GitRemote remote)
    {
        this.remote = remote;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    public String getName()
    {
        return remote.getName();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    public String getFirstUrl() throws RemoteNotFoundException
    {
        String url = this.remote.getFirstUrl();

        if (url == null) {
            throw RemoteNotFoundException.urlNotFoundForRemote(this);
        }

        return url;
    }
}
