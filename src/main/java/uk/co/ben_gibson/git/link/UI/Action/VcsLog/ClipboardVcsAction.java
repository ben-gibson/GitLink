package uk.co.ben_gibson.git.link.UI.Action.VcsLog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.GitLink;

public class ClipboardVcsAction extends VcsLogAction {

    @Override
    protected void perform(@NotNull final Project project, @NotNull final Commit commit, @NotNull final VirtualFile file) {
        GitLink.getInstance(project).copyCommit(commit, file);
    }

    @Override
    protected String displayName(@NotNull final RemoteHost remoteHost)
    {
        return String.format("Copy %s link", remoteHost.toString());
    }
}
