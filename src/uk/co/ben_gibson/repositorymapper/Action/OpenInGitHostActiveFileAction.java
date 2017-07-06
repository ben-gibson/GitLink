package uk.co.ben_gibson.repositorymapper.Action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import uk.co.ben_gibson.repositorymapper.Container;

/**
 * An action that, when triggered, opens the active file in it's remote git host.
 */
public class OpenInGitHostActiveFileAction extends AnAction
{

    @Override
    public void actionPerformed(AnActionEvent e)
    {
        Container container = ServiceManager.getService(Container.class);

        container.getLogger().notice("Hello world!");
    }

    @Override
    public void update(AnActionEvent event)
    {
        super.update(event);

        event.getPresentation().setEnabled(true);
    }
}
