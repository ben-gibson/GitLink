package uk.co.ben_gibson.git.link.Git;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcs.log.VcsFullCommitDetails;
import git4idea.GitUtil;
import git4idea.commands.GitImpl;
import git4idea.repo.GitRepository;
import uk.co.ben_gibson.git.link.Git.Exception.RepositoryNotFoundException;

public class RepositoryFactory
{
    public Repository create(Project project, VirtualFile file, Branch defaultBranch) throws RepositoryNotFoundException
    {
        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFile(file);

        if (repository == null) {
            throw new RepositoryNotFoundException();
        }

        return new Repository(new GitImpl(), repository, defaultBranch);
    }

    public Repository create(Project project, VcsFullCommitDetails commitDetails, Branch defaultBranch) throws RepositoryNotFoundException
    {
        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForRoot(commitDetails.getRoot());

        if (repository == null) {
            throw new RepositoryNotFoundException();
        }

        return new Repository(new GitImpl(), repository, defaultBranch);
    }
}
