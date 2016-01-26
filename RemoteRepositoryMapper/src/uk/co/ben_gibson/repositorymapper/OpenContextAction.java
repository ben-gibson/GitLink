package uk.co.ben_gibson.repositorymapper;

import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Settings.Mapping;
import uk.co.ben_gibson.repositorymapper.Settings.Settings;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Opens the current context in a remote repository based on the mapped settings.
 */
public class OpenContextAction extends AnAction
{

    /**
     * Open the current context.
     *
     * @param e The event.
     */
    public void actionPerformed(AnActionEvent e) {

        Context context = this.getContext(e);

        if (context == null) {
            return;
        }

        Settings settings = this.getProjectSettings(e.getProject());
        String STASH_URL = "123";

        String url = String.format(STASH_URL, context.getProject(), context.getRepository(), context.getPath());

        if (context.getCaretLinePosition() != null) {
            url += "#" + context.getCaretLinePosition().toString();
        }

        // Copy the url to clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(url), null);

        // Open in the default browser
        try {
            BrowserLauncher.getInstance().browse(new URI(url));
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * Get the context that should be opened (project, repo, path, line number, branch).
     *
     * @param e The event.
     *
     * @return uk.co.ben_gibson.repositorymapper.Context
     */
    private Context getContext(AnActionEvent e)
    {

        final Project project = e.getProject();

        if (project == null) {
            return null;
        }

        Editor editor = e.getData(LangDataKeys.EDITOR);

        Integer caretPosition = null;

        if (editor != null) {
            caretPosition = editor.getCaretModel().getLogicalPosition().line;
        }

        VirtualFile file = this.getCurrentFileOrNull(project);

        if (file == null || file.getCanonicalPath() == null) {
            return null;
        }

        Settings settings = this.getProjectSettings(e.getProject());

        Mapping mapping = this.getMappingForFileOrNull(settings.getMappingList(), file);

        if (mapping == null) {
            return null;
        }

        String path = file.getCanonicalPath().replace(mapping.getBaseDirectoryPath()+"/", "");


        return new Context(mapping.getProject(), mapping.getRepository(), path, caretPosition);
    }


    /**
     * Get the current file or return null if there isn't one.
     *
     * @param project The project from which we want to get the current/active file.
     *
     * @return VirtualFile
     */
    private VirtualFile getCurrentFileOrNull(Project project)
    {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        if (editor == null) {
            return null;
        }

        final Document document = editor.getDocument();

        return FileDocumentManager.getInstance().getFile(document);
    }


    /**
     * {@inheritDoc}
     */
    public void update(AnActionEvent e)
    {
        if (e.getProject() == null) {
            return;
        }

        VirtualFile file = this.getCurrentFileOrNull(e.getProject());

        Settings settings = this.getProjectSettings(e.getProject());

        Boolean supported = (file != null && (this.getMappingForFileOrNull(settings.getMappingList(), file) != null));

        e.getPresentation().setEnabledAndVisible(supported);
    }


    /**
     * Get the mapping for the file or null if one doesn't exist.
     *
     * @param mappings The list of mappings that have been setup.
     * @param file     The file to get a mapping for.
     *
     * @return Mapping
     */
    private Mapping getMappingForFileOrNull(ArrayList<Mapping> mappings, VirtualFile file)
    {
        if (file.getCanonicalPath() == null) {
            return null;
        }

        for(Mapping mapping : mappings){
            if (file.getCanonicalPath().startsWith(mapping.getBaseDirectoryPath(), 0)) {
                return mapping;
            }
        }

        return null;
    }


    /**
     * Get the project settings.
     *
     * @param project  The project to fetch settings for.
     *
     * @return Settings
     */
    private Settings getProjectSettings(Project project)
    {
        return ServiceManager.getService(project, Settings.class);
    }
}