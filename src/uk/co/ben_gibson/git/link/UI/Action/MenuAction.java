package uk.co.ben_gibson.git.link.UI.Action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import uk.co.ben_gibson.git.link.Git.RemoteHost;

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

        Integer caretPosition = (editor != null) ? editor.getCaretModel().getLogicalPosition().line + 1 : null;

        this.gitLink().openFile(project, file, null, caretPosition);
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
