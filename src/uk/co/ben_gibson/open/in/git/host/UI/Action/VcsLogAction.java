package uk.co.ben_gibson.open.in.git.host.UI.Action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.vcs.log.VcsFullCommitDetails;
import com.intellij.vcs.log.VcsLog;
import com.intellij.vcs.log.VcsLogDataKeys;
import git4idea.GitUtil;
import git4idea.commands.GitImpl;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.open.in.git.host.Container;
import uk.co.ben_gibson.open.in.git.host.Exception.OpenInGitHostException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteCommitDescription;
import uk.co.ben_gibson.open.in.git.host.UI.Exception.RepositoryNotFoundException;
import uk.co.ben_gibson.open.in.git.host.Git.Branch;
import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.UI.Action.Exception.ActionException;
import java.net.URL;
import java.util.List;

/**
 * An action triggered from an VCS log toolbar.
 */
public class VcsLogAction extends Action
{
    public void actionPerformed(Project project, AnActionEvent event) throws OpenInGitHostException
    {
        Container container = this.container();
        VcsLog vcsLog       = event.getData(VcsLogDataKeys.VCS_LOG);

        if (vcsLog == null) {
            throw ActionException.vcsLogNotFound();
        }

        VcsFullCommitDetails commit = vcsLog.getSelectedDetails().get(0);

        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForRoot(commit.getRoot());

        if (repository == null) {
            throw new RepositoryNotFoundException();
        }

        Repository repo = new Repository(new GitImpl(), repository, Branch.master());

        Task.Backgroundable task = new Task.Backgroundable(null, "Opening Commit In Git Host") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {

                    RemoteCommitDescription description = new RemoteCommitDescription(repo.origin(), new Commit(commit));

                    URL remoteUrl = container.remoteUrlFactory(project).createUrl(
                        description,
                        container.settings(project).getForceSSL()
                    );

                    container.extensionRunner(project).run(remoteUrl);

                }  catch (OpenInGitHostException e) {
                    container.logger(project).exception(e);
                }
            }
        };

        task.queue();
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
