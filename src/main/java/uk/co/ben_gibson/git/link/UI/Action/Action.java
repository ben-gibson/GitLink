package uk.co.ben_gibson.git.link.UI.Action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Plugin;
import uk.co.ben_gibson.git.link.Preferences;

public abstract class Action extends AnAction
{
    private final Logger logger = Logger.getInstance(ServiceManager.getService(Plugin.class).displayName());

    protected abstract boolean shouldActionBeEnabled(@NotNull final AnActionEvent event);

    protected abstract String displayName(@NotNull final RemoteHost remoteHost);

    public abstract void actionPerformed(final Project project, @NotNull final AnActionEvent event);


    @Override
    public void actionPerformed(@NotNull final AnActionEvent event)
    {
        Project project = event.getProject();

        if (project == null) {
            return;
        }

        Preferences preferences = Preferences.getInstance(project);

        logger.info(String.format("Running '%s' action.", this.displayName(preferences.getRemoteHost())));

        this.actionPerformed(project, event);
    }

    @Override
    public void update(@NotNull final AnActionEvent event)
    {
        super.update(event);

        if (event.getProject() == null) {
            event.getPresentation().setEnabled(false);
            return;
        }

        Presentation presentation = event.getPresentation();
        Preferences preferences   = Preferences.getInstance(event.getProject());

        if (!preferences.isEnabled()) {
            presentation.setEnabledAndVisible(false);
            return;
        }

        RemoteHost remoteHost = preferences.getRemoteHost();

        presentation.setText(this.displayName(remoteHost));
        presentation.setIcon(remoteHost.icon());

        presentation.setEnabledAndVisible(this.shouldActionBeEnabled(event));
    }
}
