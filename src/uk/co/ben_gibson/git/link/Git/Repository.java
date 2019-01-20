package uk.co.ben_gibson.git.link.Git;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitLocalBranch;
import git4idea.GitRemoteBranch;
import git4idea.commands.Git;
import git4idea.commands.GitCommand;
import git4idea.commands.GitCommandResult;
import git4idea.commands.GitLineHandler;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.Exception.BranchException;

public class Repository
{
    private final String defaultRemoteName;
    private final Branch defaultBranch;
    private final GitRepository repository;
    private final Git git;

    Repository(@NotNull Git git, @NotNull GitRepository repository, @NotNull Branch defaultBranch, @NotNull String defaultRemoteName)
    {
        this.git               = git;
        this.repository        = repository;
        this.defaultBranch     = defaultBranch;
        this.defaultRemoteName = defaultRemoteName;
    }

    @NotNull
    Project project()
    {
        return this.repository.getProject();
    }

    @NotNull
    VirtualFile root()
    {
        return this.repository.getRoot();
    }

    /**
     * Takes a virtual file and returns a repository file
     * <p>
     * Unlike the virtual file a repository files root is from the repositories root e.g.
     * VirtualFile: /Users/Foo/Projects/acme-demo/src/bar.java
     * RepositoryFile: src/bar.java
     */
    @NotNull
    public File repositoryFileFromVirtualFile(VirtualFile file)
    {
        String pathRelativeToRepository = file.getPath().substring(this.root().getPath().length());

        return new File(pathRelativeToRepository, file.getName());
    }

    @NotNull
    public Branch currentBranch() throws RemoteException
    {
        GitLocalBranch localBranch = this.repository.getCurrentBranch();

        // If no current branch is found, or it does not exist in the origin repository and doesn't track a remote branch then
        // the branch not found exception is thrown.

        if (localBranch != null) {
            try {
                if (this.remote().hasBranch(this, localBranch)) {
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


    public boolean isCurrentCommitOnRemote()
    {
        Commit commit = this.currentCommit();

        if (commit == null) {
            return false;
        }

        final GitLineHandler handler = new GitLineHandler(this.project(), this.root(), GitCommand.BRANCH);

        handler.addParameters("-r", "--contains", commit.hash());

        GitCommandResult result = this.git.runCommand(handler);

        if (!result.success()) {
            return false; // throw?
        }

        for(String output : result.getOutput()) {
            if (output.trim().startsWith(this.defaultRemoteName)) {
                return true;
            }
        }

        return false;
    }


    @Nullable
    public Commit currentCommit()
    {
        String revision = this.repository.getCurrentRevision();

        if (revision == null) {
            return null;
        }

        return new Commit(revision);
    }

    @NotNull
    public Remote remote() throws RemoteException
    {
        for (GitRemote remote : this.repository.getRemotes()) {
            if (remote.getName().equals(this.defaultRemoteName)) {
                return new Remote(this.git, remote);
            }
        }

        throw RemoteException.remoteNotFound(this.defaultRemoteName);
    }


    @NotNull
    private Branch defaultBranch()
    {
        return this.defaultBranch;
    }
}
