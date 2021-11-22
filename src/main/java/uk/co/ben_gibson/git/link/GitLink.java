package uk.co.ben_gibson.git.link;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Repository;
import uk.co.ben_gibson.git.link.Git.RepositoryLocator;
import uk.co.ben_gibson.git.link.UI.ExceptionRenderer;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Factory.*;
import uk.co.ben_gibson.git.link.Url.Handler.BrowserHandler;
import uk.co.ben_gibson.git.link.Url.Handler.ClipboardHandler;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifier;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifierProvider;
import java.net.URL;

public class GitLink
{
    private Logger logger = Logger.getInstance(ApplicationManager.getApplication().getService(Plugin.class).displayName());
    private final RepositoryLocator repositoryLocator;
    private final ExceptionRenderer exceptionRenderer;
    private final UrlModifierProvider urlModifierProvider;
    private final BrowserHandler browserHandler;
    private final ClipboardHandler clipboardHandler;
    private final Project project;
    private final UrlFactoryLocator urlFactoryLocator;
    private final Preferences preferences;

    GitLink(Project project) {
        this.repositoryLocator = RepositoryLocator.getInstance(project);
        this.exceptionRenderer = ExceptionRenderer.getInstance();
        this.urlModifierProvider = ApplicationManager.getApplication().getService(UrlModifierProvider.class);
        this.browserHandler = BrowserHandler.getInstance();
        this.clipboardHandler = ClipboardHandler.getInstance();
        this.urlFactoryLocator = UrlFactoryLocator.getInstance(project);
        this.project = project;
        this.preferences = Preferences.getInstance(project);
    }

    public static GitLink getInstance(Project project) {
        return project.getService(GitLink.class);
    }

    public void openFile(@NotNull final VirtualFile file, final Commit commit, final LineSelection lineSelection) {
        handleFile(this.browserHandler, file, commit, lineSelection);
    }

    public void copyFile(@NotNull final VirtualFile file, final Commit commit, final LineSelection lineSelection) {
        handleFile(this.clipboardHandler, file, commit, lineSelection);
    }

    public void openCommit(@NotNull final Commit commit, @NotNull final VirtualFile file) {
        handleCommit(this.browserHandler, commit, file);
    }

    public void copyCommit(@NotNull final Commit commit, @NotNull final VirtualFile file) {
        handleCommit(this.clipboardHandler, commit, file);
    }

    private void handleFile(
        @NotNull final UrlHandler urlHandler,
        @NotNull final VirtualFile file,
       final Commit commit,
       final LineSelection lineSelection
    ) {
        try {
            final Repository repository = repositoryLocator.locate(file);
            final UrlFactory urlFactory = urlFactoryLocator.locate();

            Task.Backgroundable task = new Task.Backgroundable(project, "GitLink - Opening File")
            {
                @Override
                public void run(@NotNull ProgressIndicator indicator)
                {
                    try {

                        URL url;
                        Commit selectedCommit = commit;

                        // If we have not been give a commit to open explicitly then use the current commit. We fallback
                        // to using the branch if the commit does not exist on the remote repository (unless we have
                        // been asked not to check the remote see https://github.com/ben-gibson/GitLink/issues/97
                        // for details).
                        if ((selectedCommit == null) && (!preferences.shouldCheckCommitExistsOnRemote() || repository.isCurrentCommitOnRemote())) {
                            selectedCommit = repository.currentCommit();
                        }

                        if (selectedCommit != null) {
                            url = urlFactory.createUrlToFileAtCommit(
                                repository.remote(),
                                repository.repositoryFileFromVirtualFile(file),
                                selectedCommit,
                                lineSelection
                            );
                        } else {
                            url = urlFactory.createUrlToFileOnBranch(
                                repository.remote(),
                                repository.repositoryFileFromVirtualFile(file),
                                repository.currentBranch(),
                                lineSelection
                            );
                        }

                        GitLink.this.handleUrl(urlHandler, url);

                    } catch (GitLinkException e) {
                        GitLink.this.exceptionRenderer.render(e);
                    }
                }
            };

            task.queue();

        } catch (GitLinkException e) {
            exceptionRenderer.render(e);
        }
    }

    private void handleCommit(@NotNull final UrlHandler urlHandler, @NotNull final Commit commit, @NotNull final VirtualFile file) {
        try {
            final Repository repository = repositoryLocator.locate(file);;
            final UrlFactory urlFactory = urlFactoryLocator.locate();

            Task.Backgroundable task = new Task.Backgroundable(project, "GitLink - Opening Commit")
            {
                @Override
                public void run(@NotNull ProgressIndicator indicator)
                {
                    try {

                        final URL url = urlFactory.createUrlToCommit(repository.remote(), commit);

                        GitLink.this.handleUrl(urlHandler, url);

                    } catch (GitLinkException e) {
                        GitLink.this.exceptionRenderer.render(e);
                    }
                }
            };

            task.queue();

        } catch (GitLinkException e) {
            exceptionRenderer.render(e);
        }
    }

    private void handleUrl(@NotNull final UrlHandler urlHandler, @NotNull URL url) throws GitLinkException {
        GitLink.this.logger.info(String.format("Generated URL '%s'", url.toString()));

        for (UrlModifier urlModifier : GitLink.this.urlModifierProvider.modifiers()) {
            if (preferences.isModifierEnabled(urlModifier)) {
                url = urlModifier.modify(url);
                GitLink.this.logger.info(String.format(
                    "Applied modifier '%s' - '%s'",
                    urlModifier.name(),
                    url.toString()
                ));
            }
        }

        GitLink.this.logger.info(String.format("Handling URL with handler '%s'", urlHandler.getClass().getSimpleName()));

        urlHandler.handle(url);
    }
}
