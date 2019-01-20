package uk.co.ben_gibson.git.link.UI.Action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.vcs.log.VcsFullCommitDetails;
import com.intellij.vcs.log.VcsLog;
import com.intellij.vcs.log.VcsLogDataKeys;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import java.util.List;

/**
 * An action triggered from an VCS log toolbar.
 */
class VcsLogAction extends Action
{

    public void actionPerformed(Project project, AnActionEvent event)
    {
        VcsLog vcsLog = event.getData(VcsLogDataKeys.VCS_LOG);

        if (vcsLog == null) {
            return;
        }

        VcsFullCommitDetails vcsCommit = vcsLog.getSelectedDetails().get(0);

        Commit commit = new Commit(vcsCommit.getId().toShortString());

        this.gitLink().openCommit(project, commit, vcsCommit.getRoot());
    }


    protected boolean shouldActionBeEnabled(AnActionEvent event)
    {
        VcsLog log = event.getData(VcsLogDataKeys.VCS_LOG);

        if (log == null) {
            return false;
        }

        List<VcsFullCommitDetails> commits = log.getSelectedDetails();

        return commits.size() == 1;
    }


    protected String displayName(RemoteHost remoteHost)
    {
        return String.format("Open commit in %s", remoteHost.toString());
    }
}
