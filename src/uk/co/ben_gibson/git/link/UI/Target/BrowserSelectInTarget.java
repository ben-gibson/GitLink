package uk.co.ben_gibson.git.link.UI.Target;

import com.intellij.ide.SelectInContext;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import uk.co.ben_gibson.git.link.Container;

public class BrowserSelectInTarget implements com.intellij.ide.SelectInTarget
{
    public boolean canSelect(SelectInContext context)
    {
        return true;
    }

    public void selectIn(SelectInContext context, boolean requestFocus)
    {
        Container container = this.container();
        Project project     = context.getProject();
        VirtualFile file    = context.getVirtualFile();

        container.runner().runForFile(project, file, this.container().openInBrowserHandler());
    }

    protected Container container()
    {
        return ServiceManager.getService(Container.class);
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
