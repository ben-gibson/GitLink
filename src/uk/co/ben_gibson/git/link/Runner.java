package uk.co.ben_gibson.git.link;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcs.log.VcsFullCommitDetails;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RepositoryNotFoundException;
import uk.co.ben_gibson.git.link.Git.Repository;
import uk.co.ben_gibson.git.link.Logger.Logger;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifier;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;
import java.net.URL;

/**
 * Acts as a facade for the plugin.
 */
public class Runner
{
    public void runForFile(Project project, VirtualFile file, UrlHandler handler)
    {
        Container container     = this.container();
        Preferences preferences = container.preferences(project);
        Repository repository;

        try {
            repository = container.repositoryFactory().create(project, file, preferences.getDefaultBranch());
        } catch (RepositoryNotFoundException e) {
            container.logger(project).warning("Git repository not found, make sure you have registered your version control root: Preferences → Version Control");
            return;
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        Integer caretPosition = (editor != null) ? editor.getCaretModel().getLogicalPosition().line + 1 : null;

        this.run(project, handler, () -> {

            UrlFactory remoteUrlFactory = container.urlFactoryProvider(project).urlFactory(preferences.getRemoteHost());

            return remoteUrlFactory.createUrl(
                new FileDescription(
                    repository.origin(),
                    repository.currentBranch(),
                    repository.fileFromVirtualFile(file),
                    caretPosition
                )
            );
        });
    }

    public void runForCommit(Project project, VcsFullCommitDetails commitDetails, UrlHandler handler)
    {
        Container container = this.container();
        Preferences preferences = container.preferences(project);
        Repository repository;

        try {
            repository = container.repositoryFactory().create(project, commitDetails, preferences.getDefaultBranch());
        } catch (RepositoryNotFoundException e) {
            container.logger(project).warning("Git repository not found, make sure you have registered your version control root: Preferences → Version Control");
            return;
        }

        this.run(project, handler, () -> {
            UrlFactory remoteUrlFactory = container.urlFactoryProvider(project).urlFactory(preferences.getRemoteHost());

            return remoteUrlFactory.createUrl(new CommitDescription(repository.origin(), new Commit(commitDetails)));
        });
    }

    private void run(Project project, UrlHandler handler, RemoteUrlGenerator generator)
    {
        Container container     = this.container();
        Preferences preferences = container.preferences(project);
        Logger logger           = container.logger(project);

        Task.Backgroundable task = new Task.Backgroundable(project, "GitLink - Processing") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {

                    URL remoteUrl = generator.generateRemoteURL();

                    logger.notice(String.format("Generated URL '%s'", remoteUrl.toString()));

                    for (UrlModifier urlModifier: container.urlModifiers()) {
                        if (preferences.isModifierEnabled(urlModifier)) {
                            remoteUrl = urlModifier.modify(remoteUrl);
                            logger.notice(String.format("Applied modifier '%s' - '%s'", urlModifier.name(), remoteUrl.toString()));
                        }
                    }

                    logger.notice(String.format("Running URL handler '%s'", handler.name()));

                    handler.handle(generator.generateRemoteURL());

                }  catch (GitLinkException e) {
                    logger.exception(e);
                }
            }
        };

        task.queue();
    }

    protected Container container()
    {
        return ServiceManager.getService(Container.class);
    }
}
