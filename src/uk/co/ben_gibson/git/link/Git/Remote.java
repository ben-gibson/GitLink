package uk.co.ben_gibson.git.link.Git;

import com.intellij.openapi.util.text.StringUtil;
import git4idea.GitLocalBranch;
import git4idea.commands.Git;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRemote;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.Exception.BranchException;

import java.net.MalformedURLException;
import java.net.URL;

public class Remote
{
    private GitRemote remote;
    private final Git git;
    private URL url;

    public Remote(Git git, GitRemote remote)
    {
        this.git    = git;
        this.remote = remote;
    }

    public String name()
    {
        return remote.getName();
    }

    public URL url() throws RemoteException
    {
        if (this.url == null) {
            String url = this.remote.getFirstUrl();

            if (url == null) {
                throw RemoteException.urlNotFoundForRemote(this);
            }

            url = StringUtil.trimEnd(url, ".git");

            url = url.replaceAll(":\\d{1,4}", ""); // remove port

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
