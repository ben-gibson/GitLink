package uk.co.ben_gibson.open.in.git.host.Git;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitLocalBranch;
import git4idea.GitRemoteBranch;
import git4idea.commands.Git;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.BranchException;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;

/**
 * Represents a git repository.
 */
public class Repository
{
    private final Branch defaultBranch;
    private final GitRepository repository;
    private final Git git;
    private Remote origin;

    public Repository(Git git, GitRepository repository, Branch defaultBranch)
    {
        this.git           = git;
        this.repository    = repository;
        this.defaultBranch = defaultBranch;
    }

    Project project()
    {
        return this.repository.getProject();
    }

    VirtualFile root()
    {
        return this.repository.getRoot();
    }

    public String relativePath(File file)
    {
        return file.path().substring(this.root().getPath().length());
    }

    public Branch currentBranch()
    {
        GitLocalBranch localBranch = this.repository.getCurrentBranch();

        // If no current branch is found, or it does not exist in the origin repository and doesn't track a remote branch then
        // the branch not found exception is thrown.

        if (localBranch != null) {
            try {
                if (this.origin.hasBranch(this, localBranch)) {
                    return new Branch(localBranch.getName());
                }
            } catch (BranchException exception) {
                GitRemoteBranch trackedBranch = localBranch.findTrackedBranch(this.repository);
                if (trackedBranch != null) {
                    return new Branch(trackedBranch.getName());
                }
            }
        }

        return this.defaultBranch();
    }

    public Remote origin() throws RemoteException
    {
        if (this.origin == null) {

            this.repository.getRemotes().stream().filter(remote -> remote.getName().equals("origin")).forEach(remote -> {
                this.origin = new Remote(this.git, remote);
            });

            if (this.origin == null) {
                throw RemoteException.originNotFound();
            }
        }

        return this.origin;
    }

    private Branch defaultBranch()
    {
        return this.defaultBranch;
    }
}
