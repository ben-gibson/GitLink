package uk.co.ben_gibson.repositorymapper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.vcs.log.VcsFullCommitDetails;
import com.intellij.vcs.log.VcsLog;
import com.intellij.vcs.log.VcsLogDataKeys;
import git4idea.GitUtil;
import git4idea.commands.GitImpl;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Context.Exception.InvalidContextException;
import uk.co.ben_gibson.repositorymapper.Notification.Notifier;
import uk.co.ben_gibson.repositorymapper.Repository.Repository;
import uk.co.ben_gibson.repositorymapper.Settings.Settings;

import java.util.List;

/**
 * Opens a commit in its Git Host.
 */
public class OpenCommitInGitHostAction extends AnAction
{

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event)
    {
        Project project = event.getProject();
        VcsLog log = event.getData(VcsLogDataKeys.VCS_LOG);

        if (project == null || log == null) {
            return;
        }

        Settings settings = ServiceManager.getService(project, Settings.class);

        try {
            Context context = this.getContext(project, log);
            Handler.getInstance().open(settings.getHost(), context, settings.getForceSSL(), settings.getCopyToClipboard());
        } catch (Exception e) {
            Notifier.errorNotification(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(@NotNull AnActionEvent event)
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
    }

    /**
     * Get the current context.
     *
     * @param project The project.
     * @param log     The vcs log.
     *
     * @return Context
     */
    @NotNull
    private Context getContext(@NotNull Project project, @NotNull VcsLog log) throws InvalidContextException
    {
        VcsFullCommitDetails commit = log.getSelectedDetails().get(0);

        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForRoot(commit.getRoot());

        if (repository == null) {
            throw new InvalidContextException("Git repository not found for project, version control root has not been registered in Preferences → Version Control");
        }

        Repository repositoryWrapper = new Repository(new GitImpl(), repository, "master");

        return new Context(repositoryWrapper, commit.getRoot(), commit.getId().toString(), null);
    }
}
