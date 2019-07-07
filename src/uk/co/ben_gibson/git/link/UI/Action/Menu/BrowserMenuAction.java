package uk.co.ben_gibson.git.link.UI.Action.Menu;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.UI.LineSelection;

public class BrowserMenuAction extends MenuAction {

    @Override
    protected void perform(@NotNull Project project, @NotNull VirtualFile file, @Nullable LineSelection lineSelection) {
        gitLink.openFile(project, file, null, lineSelection);
    }

    @Override
    protected String displayName(RemoteHost remoteHost)
    {
        return String.format("Open in %s", remoteHost.toString());
    }
}
