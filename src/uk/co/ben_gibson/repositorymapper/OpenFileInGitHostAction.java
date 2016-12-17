package uk.co.ben_gibson.repositorymapper;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.util.IconLoader;
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
 * Opens a file in its Git Host.
 */
public class OpenFileInGitHostAction extends AnAction
{

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(AnActionEvent event)
    {
        Project project = event.getProject();

        if (project == null) {
            return;
        }

        Settings settings = ServiceManager.getService(project, Settings.class);

        try {
            Context context = this.getContext(event);
            Handler.getInstance().open(settings.getHost(), context, settings.getForceSSL(), settings.getCopyToClipboard());
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
        event.getPresentation().setEnabled(this.shouldActionBeEnabled(event));

        if (event.getPresentation().isEnabled() && event.getProject() != null) {
            Settings settings = ServiceManager.getService(event.getProject(), Settings.class);
            event.getPresentation().setIcon(settings.getHost().getIcon());
        }
    }

    /**
     * Should the action be enabled.
     *
     * @param event The action event.
     *
     * @return boolean
     */
    private boolean shouldActionBeEnabled(@NotNull AnActionEvent event)
    {
        return (event.getProject() != null && event.getData(CommonDataKeys.VIRTUAL_FILE) != null);
    }

    /**
     * Get the current context.
     *
     * @param event The action event.
     *
     * @return Context
     */
    @NotNull
    private Context getContext(@NotNull AnActionEvent event) throws InvalidContextException
    {
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);
        Project project  = event.getProject();

        if (file == null || project == null) {
            throw new InvalidContextException("Active file not found for project");
        }

        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFile(file);

        if (repository == null) {
            throw new InvalidContextException("Git repository not found for project, version control root has not been registered in Preferences → Version Control");
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        Integer caretPosition = (editor != null) ? editor.getCaretModel().getLogicalPosition().line + 1 : null;

        Repository repositoryWrapper = new Repository(new GitImpl(), repository, "master");

        return new Context(repositoryWrapper, file, null, caretPosition);
    }
}