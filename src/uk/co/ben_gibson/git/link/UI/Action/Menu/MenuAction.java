package uk.co.ben_gibson.git.link.UI.Action.Menu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;
import uk.co.ben_gibson.git.link.UI.Action.Action;

/**
 * An action triggered from the view or right click menu.
 */
abstract class MenuAction extends Action
{
    abstract UrlHandler remoteUrlHandler();

    public void actionPerformed(Project project, AnActionEvent event)
    {
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);

        if (file == null || project == null) {
            return;
        }

        this.container().runner().runForFile(project, file, this.remoteUrlHandler());
    }

    protected boolean shouldActionBeEnabled(AnActionEvent event)
    {
        return (event.getData(CommonDataKeys.VIRTUAL_FILE) != null);
    }
}
