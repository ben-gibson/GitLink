package uk.co.ben_gibson.git.link.UI.Action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Container;
import uk.co.ben_gibson.git.link.Logger.Logger;
import uk.co.ben_gibson.git.link.Preferences;

public abstract class Action extends AnAction
{
    protected abstract boolean shouldActionBeEnabled(AnActionEvent event);
    protected abstract String displayName(RemoteHost remoteHost);

    public abstract void actionPerformed(Project project, AnActionEvent event);

    public void actionPerformed(AnActionEvent event)
    {
        Project project = event.getProject();

        Container container = this.container();
        Preferences settings   = container.preferences(project);
        Logger logger       = container().logger(project);

        if (project == null) {
            return;
        }

        logger.notice(String.format("Running '%s' action.", this.displayName(settings.getRemoteHost())));

        this.actionPerformed(project, event);
    }

    public void update(AnActionEvent event)
    {
        super.update(event);

        if (event.getProject() == null) {
            event.getPresentation().setEnabled(false);
            return;
        }

        RemoteHost remoteHost = this.container().preferences(event.getProject()).getRemoteHost();

        Presentation presentation = event.getPresentation();

        presentation.setText(this.displayName(remoteHost));
        presentation.setIcon(remoteHost.icon());

        presentation.setEnabledAndVisible(this.shouldActionBeEnabled(event));
    }

    protected Container container()
    {
        return ServiceManager.getService(Container.class);
    }
}
