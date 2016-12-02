package uk.co.ben_gibson.repositorymapper;

import com.intellij.ide.SelectInContext;
import com.intellij.ide.SelectInTarget;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
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
import uk.co.ben_gibson.repositorymapper.Settings.Settings;

/**
 * Opens a file in its Git Host.
 */
public class OpenFileInGitHostTarget implements SelectInTarget
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canSelect(SelectInContext context)
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectIn(SelectInContext targetContext, boolean requestFocus)
    {
        Settings settings = ServiceManager.getService(targetContext.getProject(), Settings.class);

        try {
            Context context = this.getContext(targetContext);
            Handler.getInstance().open(settings.getHost(), context, settings.getForceSSL(), settings.getCopyToClipboard());
        } catch (Exception e) {
            Notifier.errorNotification(e.getMessage());
        }
    }

    /**
     * Get the current context.
     *
     * @return Context
     */
    @NotNull
    private Context getContext(@NotNull SelectInContext targetContext) throws InvalidContextException
    {
        VirtualFile file = targetContext.getVirtualFile();

        GitRepository repository = GitUtil.getRepositoryManager(targetContext.getProject()).getRepositoryForFile(file);

        if (repository == null) {
            throw new InvalidContextException("Git repository not found for project, version control root has not been registered in Preferences → Version Control");
        }

        Repository repositoryWrapper = new Repository(new GitImpl(), repository, "master");

        Integer line = null;

        // also support "Select in Git Host" from the current editor
        Editor selectedTextEditor = FileEditorManager.getInstance(targetContext.getProject()).getSelectedTextEditor();
        FileEditor fileEditor = FileEditorManager.getInstance(targetContext.getProject()).getSelectedEditor(file);

        boolean isCurrentFileOpenedInEditor = fileEditor != null;
        boolean isEditorOpened = selectedTextEditor != null;
        if (isEditorOpened && isCurrentFileOpenedInEditor) {
            line = selectedTextEditor.getCaretModel().getLogicalPosition().line + 1;
        }
        return new Context(repositoryWrapper, file, null, line);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getToolWindowId()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getMinorViewId()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWeight()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Open in Git host";
    }
}
