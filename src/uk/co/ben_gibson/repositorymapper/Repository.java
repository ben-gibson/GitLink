package uk.co.ben_gibson.repositorymapper;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitBranch;
import git4idea.repo.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.repositorymapper.UrlFactory.UrlFactoryException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Wraps up a GitRepository to add some behaviour.
 */
public class Repository
{

    private GitRepository repository;


    /**
     * Constructor.
     *
     * @param repository The repository.
     */
    Repository(GitRepository repository)
    {
        this.repository = repository;
    }


    /**
     * Get the active branch which tracks a remote or default to master.
     *
     * @return String
     */
    @Nullable
    public String getActiveBranchNameWithRemote() throws UrlFactoryException
    {
        GitBranch branch;

        if (this.repository.getCurrentBranch() != null && this.repository.getCurrentBranch().findTrackedBranch(this.repository) != null) {
            branch = this.repository.getCurrentBranch();
        } else {
            branch = this.repository.getBranches().findBranchByName("master");
        }

        return branch != null ? branch.getName() : null;
    }


    /**
     * Get a clean url from the repositories remote origin.
     *
     * @return URL
     */
    @Nullable
    public URL getRemoteUrlFromRepository() throws MalformedURLException, UrlFactoryException
    {
        GitRemote origin = null;

        for (GitRemote remote : this.repository.getRemotes()) {
            if (remote.getName().equals("origin")) {
                origin = remote;
            }
        }

        if (origin == null) {
            return null;
        }

        if (origin.getFirstUrl() == null) {
            return null;
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
     * {@inheritDoc}
     */
    @NotNull
    public VirtualFile getRoot()
    {
        return repository.getRoot();
    }
}
