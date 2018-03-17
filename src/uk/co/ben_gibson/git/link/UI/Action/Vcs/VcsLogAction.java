package uk.co.ben_gibson.git.link.UI.Action.Vcs;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.vcs.log.VcsFullCommitDetails;
import com.intellij.vcs.log.VcsLog;
import com.intellij.vcs.log.VcsLogDataKeys;
import uk.co.ben_gibson.git.link.Container;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;
import uk.co.ben_gibson.git.link.UI.Action.Action;

import java.util.List;

/**
 * An action triggered from an VCS log toolbar.
 */
abstract class VcsLogAction extends Action
{
    abstract UrlHandler urlHandler();


    public void actionPerformed(Project project, AnActionEvent event)
    {
        VcsLog vcsLog = event.getData(VcsLogDataKeys.VCS_LOG);

        if (vcsLog == null) {
            return;
        }

        VcsFullCommitDetails vcsCommit = vcsLog.getSelectedDetails().get(0);
        Commit commit = new Commit(vcsCommit.toString());

        this.getManager().handleCommit(this.urlHandler(), project, commit, vcsCommit.getRoot());
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
}
