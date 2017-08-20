package uk.co.ben_gibson.open.in.git.host.Git;

import git4idea.GitLocalBranch;
import git4idea.commands.Git;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRemote;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.BranchException;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;

public class Remote
{
    private GitRemote remote;
    private final Git git;

    public Remote(Git git, GitRemote remote)
    {
        this.git    = git;
        this.remote = remote;
    }

    public String name()
    {
        return remote.getName();
    }

    public String url() throws RemoteException
    {
        String url = this.remote.getFirstUrl();

        if (url == null) {
            throw RemoteException.urlNotFoundForRemote(this);
        }

        return url;
    }

    /**
     * Makes a call to the remote createHostUrl to determine if the given branch exists there.
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
