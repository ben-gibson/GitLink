package uk.co.ben_gibson.open.in.git.host.Git;

import com.intellij.openapi.util.text.StringUtil;
import git4idea.commands.Git;
import git4idea.repo.GitRepository;

import java.net.URL;

/**
 * Represents a git repository.
 */
public class Repository
{
    private final String defaultBranch;
    private final GitRepository repository;
    private final Git git;

    /**
     * Constructor.
     */
    public Repository(Git git, GitRepository repository, String defaultBranch)
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
    public String getDefaultBranch()
    {
        return this.defaultBranch;
    }

    /**
     * Get the canonical origin url.
     */
    public URL getOriginUrl() throws RemoteNotFoundException, MalformedURLException
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
}
