package uk.co.ben_gibson.repositorymapper.Repository;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitBranch;
import git4idea.GitLocalBranch;
import git4idea.commands.Git;
import git4idea.commands.GitCommandResult;
import git4idea.repo.*;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.BranchNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.CouldNotFetchRemoteBranchesException;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Decorates a git repository.
 */
public class Repository
{

    @NotNull
    private GitRepository repository;

    @NotNull
    private String defaultBranch;

    @NotNull
    private Git git;

    /**
     * Constructor.
     *
     * @param repository    The git repository.
     * @param defaultBranch The default branch.
     */
    public Repository(@NotNull Git git, @NotNull GitRepository repository, @NotNull String defaultBranch)
    {
        this.git           = git;
        this.repository    = repository;
        this.defaultBranch = defaultBranch;
    }

    /**
     * Get the default branch.
     *
     * @return String
     */
    @NotNull
    public String getDefaultBranch()
    {
        return this.defaultBranch;
    }


    /**
     * Get the current branch.
     *
     * If no current branch is found, or it does not exist in the origin repository and doesn't track a remote branch then
     * the branch not found exception is thrown.
     *
     * @return String
     */
    @NotNull
    public String getCurrentBranch() throws BranchNotFoundException, RemoteNotFoundException
    {
        GitLocalBranch branch = this.repository.getCurrentBranch();

        if (branch != null) {

            try {

                if (branch.getName().equals(this.getDefaultBranch()) || this.remoteHasBranch(this.getRawOrigin(), branch)) {
                    return branch.getName();
                }

            } catch (CouldNotFetchRemoteBranchesException e) {
                // if we cannot check branches in the remote repository then we fall back to checking for a remote tracking branch.
                if (branch.findTrackedBranch(this.repository) != null) {
                    return branch.getName();
                }

            }
        }

        throw new BranchNotFoundException("Could not find the current branch");
    }

    /**
     * Does the branch exist on a given remote.
     *
     * @param remote The remote to check.
     * @param branch The branch to check.
     *
     * @return boolean
     */
    private boolean remoteHasBranch(@NotNull GitRemote remote, @NotNull GitBranch branch) throws CouldNotFetchRemoteBranchesException
    {
        GitCommandResult result = this.git.lsRemote(this.repository.getProject(), this.repository.getRoot(), remote, remote.getFirstUrl(), branch.getFullName(), "--heads");

        if (!result.success()) {
            throw new CouldNotFetchRemoteBranchesException(result.getOutputAsJoinedString());
        }

        return (result.getOutput().size() == 1);
    }

    /**
     * Get the canonical origin url.
     *
     * @param forceSSL Should we enforce SSL if the HTTP protocol is not used in origin?.
     *
     *
     * @return URL
     */
    public URL getOriginUrl(boolean forceSSL) throws RemoteNotFoundException, MalformedURLException
    {
        return this.getRemoteUrl(this.getOrigin(), forceSSL);
    }

    /**
     * Get the canonical url from a remote.
     *
     * @param remote    The remote to get the url from.
     * @param forceSSL  Should we enforce SSL if the HTTP protocol is not used in the remote?.
     *
     * @return URL
     */
    public URL getRemoteUrl(@NotNull Remote remote, boolean forceSSL) throws MalformedURLException, RemoteNotFoundException
    {
        String url = StringUtil.trimEnd(remote.getFirstUrl(), ".git");

        url = url.replaceAll(":\\d{1,4}", ""); // remove port

        if (url.startsWith("http")) {
            return new URL(url);
        }

        url = StringUtil.replace(url, "git@", "");
        url = StringUtil.replace(url, "ssh://", "");
        url = StringUtil.replace(url, "git://", "");

        String protocol = (forceSSL) ? "https" : "http";

        url = protocol + "://" + StringUtil.replace(url, ":", "/");

        return new URL(url);
    }

    /**
     * Get origin.
     *
     * @return Remote
     */
    @NotNull
    public Remote getOrigin() throws RemoteNotFoundException
    {
        return new Remote(this.getRawOrigin());
    }

    /**
     * Get the origin Git4idea remote object.
     *
     * We need to be able to access the Git4Idea remote object to use the git command
     * functionality (see remoteHasBranch()). Any remote returned from this class should
     * return our decorated version of remote.
     *
     * @return GitRemote
     */
    private GitRemote getRawOrigin() throws RemoteNotFoundException {

        for (GitRemote remote : this.repository.getRemotes()) {
            if (remote.getName().equals("origin")) {
                return remote;
            }
        }

        throw RemoteNotFoundException.originNotFound();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    public VirtualFile getRoot()
    {
        return repository.getRoot();
    }
}
