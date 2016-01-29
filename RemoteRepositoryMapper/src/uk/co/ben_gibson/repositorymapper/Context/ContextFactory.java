package uk.co.ben_gibson.repositorymapper.Context;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.history.VcsBaseRevisionAdviser;
import com.intellij.openapi.vcs.history.VcsHistoryProvider;
import com.intellij.openapi.vcs.history.VcsHistoryUtil;
import com.intellij.openapi.vcs.vfs.VcsFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.repositorymapper.Settings.Mapping;
import uk.co.ben_gibson.repositorymapper.Settings.Settings;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Context Factory
 */
public class ContextFactory
{

    /**
     * Create context.
     *
     * @param project   The active project.
     * @param settings  The settings.
     *
     * @return Context
     */
    public Context create(Project project, Settings settings) throws MalformedURLException {

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        Integer caretPosition = null;

        if (editor != null) {
            caretPosition = editor.getCaretModel().getVisualPosition().line;
        }

        VirtualFile file = this.getCurrentFile(project);

        if (file == null || file.getCanonicalPath() == null) {
            return null;
        }

        Mapping mapping = this.getMappingForFile(settings.getMappingList(), file);

        if (mapping == null) {
            return null;
        }

        String path = file.getCanonicalPath().replace(mapping.getBaseDirectoryPath()+"/", "");

        return new Context(
            settings.getHostAsURL(),
            mapping.getProject(),
            mapping.getRepository(),
            path,
            caretPosition
        );
    }


    /**
     * Get the active file.
     *
     * @param project The project which we want to get the active file from.
     *
     * @return VirtualFile
     */
    @Nullable
    private VirtualFile getCurrentFile(Project project)
    {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        if (editor == null) {
            return null;
        }

        final Document document = editor.getDocument();

        return FileDocumentManager.getInstance().getFile(document);
    }


    /**
     * Get the mapping for a file.
     *
     * @param mappings A collection of mappings.
     * @param file     The file to get a mapping for.
     *
     * @return Mapping
     */
    @Nullable
    private Mapping getMappingForFile(ArrayList<Mapping> mappings, VirtualFile file)
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
}
