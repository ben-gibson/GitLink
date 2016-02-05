package uk.co.ben_gibson.repositorymapper.UrlFactory;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.ContextProviderException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Abstract Url factory
 */
abstract public class AbstractUrlFactory implements UrlFactory
{


    /**
     * Get the file path relative to the repository.
     *
     * @return String
     */
    protected String getRepositoryRelativeFilePath(@NotNull GitRepository repository, @NotNull VirtualFile file)
    {
        return file.getPath().substring(repository.getRoot().getPath().length());
    }


    /**
     * Get a clean url from the repositories remote origin.
     *
     * @return URL
     */
    @NotNull
    protected URL getRemoteHostFromRepository(@NotNull GitRepository repository) throws MalformedURLException, ContextProviderException
    {
        GitRemote origin = null;

        for (GitRemote remote : repository.getRemotes()) {
            if (remote.getName().equals("origin")) {
                origin = remote;
            }
        }

        if (origin == null) {
            throw ContextProviderException.originRemoteNotFound(repository);
        }

        if (origin.getFirstUrl() == null) {
            throw ContextProviderException.urlNotFoundForRemote(origin);
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
