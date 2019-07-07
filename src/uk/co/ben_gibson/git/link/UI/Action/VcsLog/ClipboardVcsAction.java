package uk.co.ben_gibson.git.link.UI.Action.VcsLog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.RemoteHost;

public class ClipboardVcsAction extends VcsLogAction {

    @Override
    protected void perform(@NotNull Project project, @NotNull Commit commit, @NotNull VirtualFile file) {
        gitLink.copyCommit(project, commit, file);
    }

    @Override
    protected String displayName(RemoteHost remoteHost)
    {
        return String.format("Copy %s link", remoteHost.toString());
    }
}
