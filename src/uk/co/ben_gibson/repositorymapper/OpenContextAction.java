package uk.co.ben_gibson.repositorymapper;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Repository.Repository;
import uk.co.ben_gibson.repositorymapper.Settings.Settings;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.repositorymapper.UrlFactory.UrlFactoryProvider;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;

/**
 * Opens the current context in a remote repository.
 */
public class OpenContextAction extends AnAction
{

    /**
     * Open the current context.
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

            Context context = this.getContext(project);

            if (context == null) {
                return;
            }

            UrlFactoryProvider urlFactoryProvider = ServiceManager.getService(UrlFactoryProvider.class);

            URL url = urlFactoryProvider.getUrlFactoryForProvider(settings.getRepositoryProvider()).getUrlFromContext(context);

            if (settings.getCopyToClipboard()) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(url.toString()), null);
            }

            BrowserLauncher.getInstance().browse(url.toURI());

        } catch (Exception e) {
            Messages.showErrorDialog(event.getProject(), e.getMessage(), "Error");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(AnActionEvent event)
    {
        Project project = event.getProject();

        Boolean enabled = (project != null) && (this.getContext(project) != null);

        event.getPresentation().setEnabled(enabled);
    }


    /**
     * Get the current context.
     *
     * @return Context
     */
    @Nullable
    public Context getContext(@NotNull Project project)
    {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        if (editor == null) {
            return null;
        }

        VirtualFile file =  FileDocumentManager.getInstance().getFile(editor.getDocument());

        if (file == null) {
            return null;
        }

        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFile(file);

        if (repository == null) {
            return null;
        }

        Integer caretPosition = editor.getCaretModel().getLogicalPosition().line + 1;

        Repository repositoryWrapper = new Repository(repository, "master");

        return new Context(repositoryWrapper, file, caretPosition);
    }
}