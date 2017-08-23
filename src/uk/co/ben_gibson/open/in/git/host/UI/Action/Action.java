package uk.co.ben_gibson.open.in.git.host.UI.Action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.open.in.git.host.Container;
import uk.co.ben_gibson.open.in.git.host.Exception.OpenInGitHostException;
import uk.co.ben_gibson.open.in.git.host.Logger.Logger;

abstract class Action extends AnAction
{
    protected abstract boolean shouldActionBeEnabled(AnActionEvent event);

    abstract void actionPerformed(Project project, AnActionEvent event) throws OpenInGitHostException;

    public void actionPerformed(AnActionEvent event)
    {
        Project project = event.getProject();

        Logger logger = container().logger(project);

        if (project == null) {
            return;
        }

        try {
            this.actionPerformed(project, event);
        } catch (OpenInGitHostException exception) {
            logger.exception(exception);
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

        if (event.getPresentation().isEnabled()) {
            event.getPresentation().setIcon(this.container().remoteHost(event.getProject()).icon());
        }
    }

    Container container()
    {
        return ServiceManager.getService(Container.class);
    }
}
