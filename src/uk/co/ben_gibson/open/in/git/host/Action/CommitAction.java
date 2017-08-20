package uk.co.ben_gibson.open.in.git.host.Action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.vcs.log.VcsFullCommitDetails;
import com.intellij.vcs.log.VcsLog;
import com.intellij.vcs.log.VcsLogDataKeys;
import git4idea.GitUtil;
import git4idea.commands.GitImpl;
import git4idea.repo.GitRepository;
import uk.co.ben_gibson.open.in.git.host.Action.Exception.ActionException;
import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.OpenInGitHostException;
import java.net.URL;
import java.util.List;

/**
 * An action that, when triggered, builds a url to the remote repository from the commit and runs it though the registered extensions.
 */
public class CommitAction extends Action
{
    protected URL handleAction(AnActionEvent event) throws OpenInGitHostException
    {
        Project project = event.getProject();
        VcsLog log = event.getData(VcsLogDataKeys.VCS_LOG);

        if (project == null) {
            throw ActionException.projectNotFound();
        }

        if (log == null) {
            throw ActionException.vcsLogNotFound();
        }

        VcsFullCommitDetails commit = log.getSelectedDetails().get(0);

        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForRoot(commit.getRoot());

        if (repository == null) {
            this.logger().warning("Git repository not found, make sure you have registered your version control root: Preferences → Version Control");
            return null;
        }

        return this.remoteUrlFactory().createRemoteUrlToCommit(
            this.settings().remoteHost(),
            new Repository(new GitImpl(), repository, "master"),
            new Commit(commit)
        );
    }

    public void update(AnActionEvent event)
    {
        event.getPresentation().setEnabledAndVisible(true);

        Project project = event.getProject();

        if (project == null) {
            event.getPresentation().setEnabledAndVisible(false);
            return;
        }

        VcsLog log = event.getData(VcsLogDataKeys.VCS_LOG);

        if (log == null) {
            event.getPresentation().setEnabledAndVisible(false);
            return;
        }

        List<VcsFullCommitDetails> commits = log.getSelectedDetails();

        if (commits.size() != 1) {
            event.getPresentation().setEnabledAndVisible(false);
            return;
        }

        event.getPresentation().setEnabledAndVisible(true);

        event.getPresentation().setIcon(this.settings().remoteHost().icon());
    }
}
