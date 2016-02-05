package uk.co.ben_gibson.repositorymapper.UrlFactory;

import com.intellij.openapi.util.text.StringUtil;
import git4idea.GitBranch;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Abstract Url factory
 */
abstract public class AbstractUrlFactory implements UrlFactory
{

    /**
     * Get the current branch or default to master.
     *
     * @return GitBranch
     */
    @NotNull
    protected GitBranch getBranch(@NotNull GitRepository repository) throws UrlFactoryException {

        GitBranch branch;

        if (repository.getCurrentBranch() != null && repository.getCurrentBranch().findTrackedBranch(repository) != null) {
            branch = repository.getCurrentBranch();
        } else {
            branch = repository.getBranches().findBranchByName("master");
        }

        if (branch == null) {
            throw UrlFactoryException.remoteBranchNotFound();
        }

        return branch;
    }


    /**
     * Get a clean url from the repositories remote origin.
     *
     * @return URL
     */
    @NotNull
    protected URL getRemoteUrlFromRepository(@NotNull GitRepository repository) throws MalformedURLException, UrlFactoryException
    {
        GitRemote origin = null;

        for (GitRemote remote : repository.getRemotes()) {
            if (remote.getName().equals("origin")) {
                origin = remote;
            }
        }

        if (origin == null) {
            throw UrlFactoryException.originRemoteNotFound(repository);
        }

        if (origin.getFirstUrl() == null) {
            throw UrlFactoryException.urlNotFoundForRemote(origin);
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
}
