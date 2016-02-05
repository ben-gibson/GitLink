package uk.co.ben_gibson.repositorymapper.Repository;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.repo.*;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.BranchNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Wraps up a GitRepository to add some behaviour.
 */
public class Repository
{

    private GitRepository repository;
    private String defaultBranch;


    /**
     * Constructor.
     *
     * @param repository    The repository.
     * @param defaultBranch The default branch.
     */
    public Repository(@NotNull GitRepository repository, @NotNull String defaultBranch)
    {
        this.repository    = repository;
        this.defaultBranch = defaultBranch;
    }


    /**
     * Get default branch.
     *
     * @return String
     */
    @NotNull
    public String getDefaultBranch()
    {
        return this.defaultBranch;
    }



    /**
     * Get the active branch which tracks a remote or default to master.
     *
     * @return String
     */
    @NotNull
    public String getActiveBranchWithRemote() throws BranchNotFoundException
    {
        if (this.repository.getCurrentBranch() != null && this.repository.getCurrentBranch().findTrackedBranch(this.repository) != null) {
           return this.repository.getCurrentBranch().getName();
        }

        throw BranchNotFoundException.activeBranchWithRemoteTrackingNotFound();
    }


    /**
     * Get a url from a remote.
     *
     * @return URL
     */
    public URL getOriginUrl() throws RemoteNotFoundException, MalformedURLException
    {
        Remote origin = this.getOrigin();

        if (origin.getFirstUrl() == null) {
            throw RemoteNotFoundException.urlNotFoundForRemote(origin);
        }

        String url = StringUtil.trimEnd(origin.getFirstUrl(), ".git");

        url = url.replaceAll(":\\d{1,4}", ""); // remove port

        if (url.startsWith("http")) {
            return new URL(url);
        }

        url = StringUtil.replace(url, "git@", "");
        url = StringUtil.replace(url, "ssh://", "");

        url = "https://" + StringUtil.replace(url, ":", "/");

        return new URL(url);
    }


    /**
     * Fetch the remote origin
     *
     * @return Remote
     */
    @NotNull
    public Remote getOrigin() throws RemoteNotFoundException
    {

        for (GitRemote remote : this.repository.getRemotes()) {
            if (remote.getName().equals("origin")) {
                return new Remote(remote);
            }
        }

        throw RemoteNotFoundException.remoteOriginNotFound();
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
