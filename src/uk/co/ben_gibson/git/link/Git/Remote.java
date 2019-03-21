package uk.co.ben_gibson.git.link.Git;

import com.intellij.openapi.util.text.StringUtil;
import git4idea.GitLocalBranch;
import git4idea.commands.Git;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRemote;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.Exception.BranchException;

import java.net.MalformedURLException;
import java.net.URL;

public class Remote
{
    private GitRemote remote;
    private final Git git;
    private URL url;

    public Remote(@NotNull Git git, @NotNull GitRemote remote)
    {
        this.git    = git;
        this.remote = remote;
    }

    @NotNull
    public String name()
    {
        return remote.getName();
    }

    @NotNull
    public URL url() throws RemoteException
    {
        if (this.url == null) {
            String url = this.remote.getFirstUrl();

            if (url == null) {
                throw RemoteException.urlNotFoundForRemote(this);
            }

            url = StringUtil.trim(url);
            url = StringUtil.trimEnd(url, ".git");

            // Do not try to remove the port if the URL uses the SSH protocol in the SCP syntax. For example
            // 'git@github.com:foo.git'. This syntax does not support port definitions. Attempting to remove the port
            // will result in an invalid URL when the repository name is made up of digits.
            // See https://github.com/ben-gibson/GitLink/issues/94
            if (!url.startsWith("git@")) {
                url = url.replaceAll(":\\d{1,5}", ""); // remove the port
            }

            if (!url.startsWith("http")) {
                url = StringUtil.replace(url, "git@", "");
                url = StringUtil.replace(url, "ssh://", "");
                url = StringUtil.replace(url, "git://", "");
                url = String.format(
                    "%s://%s",
                    "http",
                    StringUtil.replace(url, ":", "/")
                );
            }

            try {
                this.url = new URL(url);
            } catch (MalformedURLException e) {
                throw RemoteException.urlNotFoundForRemote(this);
            }
        }

        return this.url;
    }

    /**
     * Asks the remote repository if it's aware of a given branch.
     */
    public boolean hasBranch(Repository repository, GitLocalBranch branch) throws BranchException
    {
        GitCommandResult result = this.git.lsRemote(
            repository.project(),
            repository.root(),
            this.remote,
            this.remote.getFirstUrl(),
            branch.getFullName(),
            "--heads"
        );

        if (!result.success()) {
            throw BranchException.couldNotFetchBranchesFromRemoteRepository(result.getOutputAsJoinedString());
        }

        return (result.getOutput().size() == 1);
    }
}
