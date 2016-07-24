package uk.co.ben_gibson.repositorymapper;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitUtil;
import git4idea.commands.GitImpl;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Context.Exception.InvalidContextException;
import uk.co.ben_gibson.repositorymapper.Notification.Notifier;
import uk.co.ben_gibson.repositorymapper.Repository.Repository;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.repositorymapper.Settings.Settings;

/**
 * Menu item action.
 */
public class Action extends AnAction
{

    /**
     * Handle the current context.
     *
     * @param event The event.
     */
    public void actionPerformed(AnActionEvent event)
    {
        Project project = event.getProject();

        if (project == null) {
            return;
        }

        Settings settings = ServiceManager.getService(project, Settings.class);

        try {
            Handler.getInstance().open(
                settings.getHost(),
                this.getContext(project),
                settings.getForceSSL(),
                settings.getCopyToClipboard()
            );
        } catch (Exception e) {
            Notifier.errorNotification(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(AnActionEvent event)
    {
        event.getPresentation().setEnabled(this.shouldActionBeEnabled(event.getProject()));
    }

    /**
     * Should the action be enabled.
     *
     * @param project The project.
     * @return boolean
     */
    private boolean shouldActionBeEnabled(@Nullable Project project)
    {
        if (project == null) {
            return false;
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        if (editor == null) {
            return false;
        }

        VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());

        return file != null;
    }

    /**
     * Get the current context.
     *
     * @return Context
     */
    @NotNull
    private Context getContext(@NotNull Project project) throws InvalidContextException
    {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        if (editor == null) {
            throw new InvalidContextException("File editor not found for project");
        }

        VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());

        if (file == null) {
            throw new InvalidContextException("Active file not found for project");
        }

        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFile(file);

        if (repository == null) {
            throw new InvalidContextException("Git repository not found for project, version control root has not been registered in Preferences → Version Control");
        }

        Integer caretPosition = editor.getCaretModel().getLogicalPosition().line + 1;

        Repository repositoryWrapper = new Repository(new GitImpl(), repository, "master");

        return new Context(repositoryWrapper, file, caretPosition);
    }
}