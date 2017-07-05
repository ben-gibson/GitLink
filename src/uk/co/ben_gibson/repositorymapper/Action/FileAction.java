package uk.co.ben_gibson.repositorymapper.Action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import uk.co.ben_gibson.repositorymapper.Logger.Handler.EventLogHandler;
import uk.co.ben_gibson.repositorymapper.Logger.Handler.IdeaHandler;
import uk.co.ben_gibson.repositorymapper.Logger.Logger;

/**
 * A file based action.
 */
public class FileAction extends AnAction
{

    @Override
    public void actionPerformed(AnActionEvent e)
    {
        Logger logger = ServiceManager.getService(Logger.class);

        logger.registerHandler(new EventLogHandler());
        logger.registerHandler(new IdeaHandler(com.intellij.openapi.diagnostic.Logger.getInstance("OpenInGitHost")));

        logger.notice("Hello world!");
    }

    @Override
    public void update(AnActionEvent event)
    {
        super.update(event);

        event.getPresentation().setEnabled(true);
    }
}
