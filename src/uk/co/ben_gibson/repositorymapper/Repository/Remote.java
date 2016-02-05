package uk.co.ben_gibson.repositorymapper.Repository;

import git4idea.repo.GitRemote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for a git remote.
 */
public class Remote
{

    GitRemote remote;

    /**
     * Constructor.
     *
     * @param remote the git remote to wrap
     */
    public Remote(GitRemote remote)
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
    @Nullable
    public String getFirstUrl()
    {
        return remote.getFirstUrl();
    }
}
