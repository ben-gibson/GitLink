package uk.co.ben_gibson.open.in.git.host.UI.Action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
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
import uk.co.ben_gibson.open.in.git.host.Exception.OpenInGitHostException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteFileDescription;
import uk.co.ben_gibson.open.in.git.host.UI.Exception.RepositoryNotFoundException;
import uk.co.ben_gibson.open.in.git.host.Git.Branch;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.UI.Action.Exception.ActionException;
import java.net.URL;

/**
 * An action triggered from the view or right click menu.
 */
public class MenuAction extends Action
{
    public void actionPerformed(Project project, AnActionEvent event) throws OpenInGitHostException
    {
        Container container = this.container();
        VirtualFile file    = event.getData(CommonDataKeys.VIRTUAL_FILE);

        if (file == null) {
            throw ActionException.fileNotFound();
        }

        Editor editor            = FileEditorManager.getInstance(project).getSelectedTextEditor();
        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFile(file);

        if (repository == null) {
            throw new RepositoryNotFoundException();
        }

        Integer caretPosition = (editor != null) ? editor.getCaretModel().getLogicalPosition().line + 1 : null;

        Repository repo = new Repository(new GitImpl(), repository, Branch.master());

        Task.Backgroundable task = new Task.Backgroundable(null, "Opening File In Git Host") {
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
                    container.logger(project).exception(e);
                }
            }
        };

        task.queue();
    }

    protected boolean shouldActionBeEnabled(AnActionEvent event)
    {
        return (event.getData(CommonDataKeys.VIRTUAL_FILE) != null);
    }
}
