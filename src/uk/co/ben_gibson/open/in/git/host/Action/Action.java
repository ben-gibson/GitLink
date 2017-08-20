package uk.co.ben_gibson.open.in.git.host.Action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.open.in.git.host.Container;
import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import uk.co.ben_gibson.open.in.git.host.Logger.Logger;
import uk.co.ben_gibson.open.in.git.host.OpenInGitHostException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.Settings;
import java.net.URL;

abstract class Action extends AnAction
{
    Settings settings;
    Logger logger;
    RemoteUrlFactory remoteUrlFactory;

    protected abstract URL handleAction(Project project, AnActionEvent event) throws OpenInGitHostException;
    protected abstract boolean shouldActionBeEnabled(AnActionEvent event);

    public void actionPerformed(AnActionEvent event)
    {
        Project project = event.getProject();

        if (project == null) {
            logger.error("Project not found");
            return;
        }

        Container container= ServiceManager.getService(Container.class);

        // Dody hack as I don't have control of the initialisation of this class!
        this.logger           = container.logger(project);
        this.settings         = container.settings(project);
        this.remoteUrlFactory = container.remoteUrlFactory(project);

        try {

            this.logger.notice(String.format("%s - has been triggered", this.getClass().getName()));

            URL remoteUrl = this.handleAction(project, event);

            // Remote URL could not be created, assume some alert has been set by the sub class to explain why!
            if (remoteUrl == null) {
                return;
            }

            this.logger.notice(String.format("Created remote url '%s'", remoteUrl.toString()));

            if (!this.settings.hasEnabledExtensions()) {
                this.logger.warning("You have no extensions enabled, enable some: Preferences → Open in Git host");
            }

            for (Extension extension: container.registeredExtensions()) {
                if (this.settings.getEnabledExtensions(extension)) {
                    this.logger.notice(String.format("Running extension '%s'", extension.displayName()));
                    extension.handle(remoteUrl);
                }
            }
        } catch (OpenInGitHostException exception) {
            this.logger.error(String.format("Could not open file in remote repository ('%s')", exception.getMessage()));
        }
    }

    public void update(AnActionEvent event)
    {
        super.update(event);

        if (event.getProject() == null) {
            event.getPresentation().setEnabled(false);
            return;
        }

        event.getPresentation().setEnabledAndVisible(this.shouldActionBeEnabled(event));

        Container container= ServiceManager.getService(Container.class);

        if (event.getPresentation().isEnabled()) {
            event.getPresentation().setIcon(container.settings(event.getProject()).getRemoteHost().icon());
        }
    }
}
