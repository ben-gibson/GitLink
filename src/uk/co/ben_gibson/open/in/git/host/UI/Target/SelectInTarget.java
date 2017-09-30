package uk.co.ben_gibson.open.in.git.host.UI.Target;

import com.intellij.ide.SelectInContext;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitUtil;
import git4idea.commands.GitImpl;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.open.in.git.host.Container;
import uk.co.ben_gibson.open.in.git.host.Logger.Logger;
import uk.co.ben_gibson.open.in.git.host.UI.Exception.RepositoryNotFoundException;
import uk.co.ben_gibson.open.in.git.host.Exception.OpenInGitHostException;
import uk.co.ben_gibson.open.in.git.host.Git.Branch;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteFileDescription;
import java.net.URL;

/**
 * Extends the select in target menu.
 */
public class SelectInTarget implements com.intellij.ide.SelectInTarget
{
    public boolean canSelect(SelectInContext context)
    {
        return true;
    }

    public void selectIn(SelectInContext context, boolean requestFocus)
    {
        Container container = this.container();
        Project project     = context.getProject();
        VirtualFile file    = context.getVirtualFile();

        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFile(file);

        Logger logger = container.logger(project);

        try {

            if (repository == null) {
                throw new RepositoryNotFoundException();
            }

            Repository repo = new Repository(new GitImpl(), repository, Branch.master());

            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

            Integer caretPosition = (editor != null) ? editor.getCaretModel().getLogicalPosition().line + 1 : null;

            Task.Backgroundable task = new Task.Backgroundable(null, "Opening Target In Git Host") {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    try {

                        RemoteFileDescription description = new RemoteFileDescription(
                            repo.origin(),
                            repo.currentBranch(),
                            repo.fileFromVirtualFile(file),
                            caretPosition
                        );

                        URL remoteUrl = container.remoteUrlFactory(project).createUrl(
                            description,
                            container.settings(project).getForceSSL()
                        );

                        container.extensionRunner(project).run(remoteUrl);

                    }  catch (OpenInGitHostException e) {
                        logger.exception(e);
                    }
                }
            };

            task.queue();

        } catch (OpenInGitHostException exception) {
            logger.exception(exception);
        }
    }

    private Container container()
    {
        return ServiceManager.getService(Container.class);
    }

    public String getToolWindowId()
    {
        return null;
    }

    public String getMinorViewId()
    {
        return null;
    }

    public float getWeight()
    {
        return 0;
    }

    public String toString()
    {
        return "Open in Git Host";
    }
}
