package uk.co.ben_gibson.git.link.Git;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitUtil;
import git4idea.commands.GitImpl;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Exception.RepositoryNotFoundException;

public class RepositoryFactory
{

    @NotNull
    public Repository create(@NotNull Project project, @NotNull VirtualFile file, @NotNull Branch defaultBranch, @NotNull String defaultRemoteName) throws RepositoryNotFoundException
    {
        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFile(file);

        if (repository == null) {
            throw new RepositoryNotFoundException();
        }

        return new Repository(new GitImpl(), repository, defaultBranch, defaultRemoteName);
    }
}
