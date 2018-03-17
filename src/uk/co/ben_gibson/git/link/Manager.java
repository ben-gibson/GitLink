package uk.co.ben_gibson.git.link;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Repository;
import uk.co.ben_gibson.git.link.Git.RepositoryFactory;
import uk.co.ben_gibson.git.link.UI.ExceptionRenderer;
import uk.co.ben_gibson.git.link.Url.Factory.*;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifier;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifierProvider;
import java.net.URL;
import java.util.Objects;

public class Manager
{
    private Logger logger = Logger.getInstance(Plugin.createDefault().displayName());
    private RepositoryFactory repositoryFactory;
    private ExceptionRenderer exceptionRenderer;
    private UrlModifierProvider urlModifierProvider;


    Manager(
        RepositoryFactory repositoryFactory,
        ExceptionRenderer exceptionRenderer,
        UrlModifierProvider urlModifierProvider
    ) {
        this.repositoryFactory = repositoryFactory;
        this.exceptionRenderer = exceptionRenderer;
        this.urlModifierProvider = urlModifierProvider;
    }


    public void handleFile(
        @NotNull UrlHandler handler,
        @NotNull Project project,
        @NotNull VirtualFile file,
        @Nullable Commit commit,
        @Nullable Integer caretPosition
    ) {
        try {
            Preferences preferences               = Preferences.getInstance(project);
            UrlFactoryProvider urlFactoryProvider = UrlFactoryProvider.fromPrefernces(preferences);

            Repository repository = this.repositoryFactory.create(
                project,
                file,
                preferences.defaultBranch,
                preferences.remoteName
            );

            this.run(
                project,
                handler,
                () -> {
                    UrlFactory remoteUrlFactory = urlFactoryProvider.forRemoteHost(preferences.remoteHost);

                    // If we haven't been given an explicit commit to use and we either don't have a current commit or it
                    // doesn't exist on the remote then createDefault a URL pointing to the current branch as it's the best we can
                    // do.

                    if (!remoteUrlFactory.canOpenFileAtCommit() || (commit == null && !repository.isCurrentCommitOnRemote())) {
                        return remoteUrlFactory.createUrlToFileOnBranch(
                            repository.remote(),
                            repository.repositoryFileFromVirtualFile(file),
                            repository.currentBranch(),
                            caretPosition
                        );
                    }

                    return remoteUrlFactory.createUrlToFileAtCommit(
                        repository.remote(),
                        repository.repositoryFileFromVirtualFile(file),
                        Objects.requireNonNull((commit != null) ? commit : repository.currentCommit()),
                        caretPosition
                    );
                }
            );
        } catch (GitLinkException e) {
            this.exceptionRenderer.render(e);
        }
    }


    public void handleCommit(@NotNull UrlHandler handler, @NotNull Project project, @NotNull Commit commit, @NotNull VirtualFile file)
    {
        try {
            Preferences preferences = Preferences.getInstance(project);
            UrlFactoryProvider urlFactoryProvider = UrlFactoryProvider.fromPrefernces(preferences);
            Repository repository = this.repositoryFactory.create(project, file, preferences.defaultBranch, preferences.remoteName);

            this.run(
                project,
                handler,
                () -> {
                    UrlFactory remoteUrlFactory = urlFactoryProvider.forRemoteHost(preferences.remoteHost);

                    return remoteUrlFactory.createUrlToCommit(repository.remote(), commit);
                }
            );
        } catch (GitLinkException e) {
            this.exceptionRenderer.render(e);
        }
    }


    private void run(Project project, UrlHandler handler, RemoteUrlGenerator generator)
    {
        Preferences preferences = Preferences.getInstance(project);

        Task.Backgroundable task = new Task.Backgroundable(project, "GitLink - Processing")
        {
            @Override
            public void run(@NotNull ProgressIndicator indicator)
            {
                try {

                    URL remoteUrl = generator.generateRemoteURL();

                    Manager.this.logger.info(String.format("Generated URL '%s'", remoteUrl.toString()));

                    for (UrlModifier urlModifier : Manager.this.urlModifierProvider.modifiers()) {
                        if (preferences.isModifierEnabled(urlModifier)) {
                            remoteUrl = urlModifier.modify(remoteUrl);
                            Manager.this.logger.info(String.format("Applied modifier '%s' - '%s'", urlModifier.name(), remoteUrl.toString()));
                        }
                    }

                    Manager.this.logger.info(String.format("Running URL handler '%s'", handler.name()));

                    handler.handle(remoteUrl);

                } catch (GitLinkException e) {
                    Manager.this.exceptionRenderer.render(e);
                }
            }
        };

        task.queue();
    }
}
