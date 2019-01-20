package uk.co.ben_gibson.git.link.UI.Extensions;

import com.intellij.ide.SelectInContext;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import uk.co.ben_gibson.git.link.GitLink;
import uk.co.ben_gibson.git.link.Preferences;

public class BrowserSelectInTarget implements com.intellij.ide.SelectInTarget
{
    public boolean canSelect(SelectInContext context)
    {
        Preferences preferences = Preferences.getInstance(context.getProject());

        if (!preferences.isEnabled()) {
            return false;
        }

        return true;
    }


    public void selectIn(SelectInContext context, boolean requestFocus)
    {
        Project project  = context.getProject();
        VirtualFile file = context.getVirtualFile();
        GitLink gitLink  = ServiceManager.getService(GitLink.class);

        gitLink.openFile(project, file, null, null);
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
