package uk.co.ben_gibson.git.link.UI.Extensions;

import com.intellij.ide.SelectInContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import uk.co.ben_gibson.git.link.Container;
import uk.co.ben_gibson.git.link.Manager;
import uk.co.ben_gibson.git.link.Url.Handler.OpenInBrowserHandler;

public class BrowserSelectInTarget implements com.intellij.ide.SelectInTarget
{
    public boolean canSelect(SelectInContext context)
    {
        return true;
    }


    public void selectIn(SelectInContext context, boolean requestFocus)
    {
        Project project = context.getProject();
        VirtualFile file = context.getVirtualFile();
        Manager manager  = Container.getInstance().manager();
        OpenInBrowserHandler handler  = Container.getInstance().openInBrowserHandler();

        manager.handleFile(handler, project, file, null, null);
    }


    public String getToolWindowId()
    {
        return null;
    }


    public String getMinorViewId()
    {
        return null;
    }


    public float getWeight()
    {
        return 0;
    }


    public String toString()
    {
        return "Browser (GitLink)";
    }
}
