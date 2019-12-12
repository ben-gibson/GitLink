package uk.co.ben_gibson.git.link.UI.Action.Menu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.UI.Action.Action;
import uk.co.ben_gibson.git.link.UI.LineSelection;

import java.util.List;

/**
 * An action triggered from the view or right click menu.
 */
abstract class MenuAction extends Action
{
    protected abstract void perform(@NotNull final Project project, @NotNull final VirtualFile file, @Nullable final LineSelection lineSelection);

    @Override
    public void actionPerformed(final Project project, @NotNull final AnActionEvent event)
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

        this.perform(project, file, lineSelection);
    }

    @NotNull
    private LineSelection getLineSelection(@NotNull final Editor editor)
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

    @Override
    protected boolean shouldActionBeEnabled(@NotNull final AnActionEvent event)
    {
        return (event.getData(CommonDataKeys.VIRTUAL_FILE) != null);
    }
}
