package uk.co.ben_gibson.git.link.Git;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.LocalFilePath;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Exception.RepositoryNotFoundException;
import uk.co.ben_gibson.git.link.Preferences;

public class RepositoryLocator
{
    private final Project project;

    public RepositoryLocator(final Project project) {
        this.project = project;
    }

    public static RepositoryLocator getInstance(Project project) {
        return ServiceManager.getService(project, RepositoryLocator.class);
    }

    @NotNull
    public Repository locate(@NotNull final VirtualFile file) throws RepositoryNotFoundException {
        LocalFilePath path = new LocalFilePath(file.getPath(), file.isDirectory());
        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFileQuick(path);

        if (repository == null) {
            throw new RepositoryNotFoundException();
        }

        Preferences preferences = Preferences.getInstance(project);

        return new Repository(repository, preferences.getDefaultBranch(), preferences.remoteName);
    }
}
