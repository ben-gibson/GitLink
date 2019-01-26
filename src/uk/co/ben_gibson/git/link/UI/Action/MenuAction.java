package uk.co.ben_gibson.git.link.UI.Action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.UI.LineSelection;

import java.util.List;

/**
 * An action triggered from the view or right click menu.
 */
class MenuAction extends Action
{

    public void actionPerformed(Project project, AnActionEvent event)
    {
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);

        if (file == null || project == null) {
            return;
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        LineSelection lineSelection = null;

        if (editor != null) {
            lineSelection = this.getLineSelection(editor);
        }

        this.gitLink().openFile(project, file, null, lineSelection);
    }


    @NotNull
    private LineSelection getLineSelection(@NotNull Editor editor)
    {
        CaretModel caretModel        = editor.getCaretModel();
        List<CaretState> caretStates = caretModel.getCaretsAndSelections();

        if (caretStates.size() < 1) {
            return new LineSelection(caretModel.getLogicalPosition().line + 1);
        }

        CaretState caretState = caretStates.get(0);
        LogicalPosition start = caretState.getSelectionStart();
        LogicalPosition end   = caretState.getSelectionEnd();

        if (start == null || end == null) {
            return new LineSelection(caretModel.getLogicalPosition().line + 1);
        }

        return new LineSelection(start.line + 1, end.line + 1);
    }


    protected boolean shouldActionBeEnabled(AnActionEvent event)
    {
        return (event.getData(CommonDataKeys.VIRTUAL_FILE) != null);
    }


    protected String displayName(RemoteHost remoteHost)
    {
        return String.format("Open in %s", remoteHost.toString());
    }

}
