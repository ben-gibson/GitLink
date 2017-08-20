package uk.co.ben_gibson.open.in.git.host.Action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import uk.co.ben_gibson.open.in.git.host.Container;
import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import uk.co.ben_gibson.open.in.git.host.Logger.Logger;
import uk.co.ben_gibson.open.in.git.host.OpenInGitHostException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.Settings;

import java.net.URL;

abstract class Action extends AnAction
{
    private Container container;

    protected abstract URL handleAction(AnActionEvent event) throws OpenInGitHostException;

    public void actionPerformed(AnActionEvent event)
    {
        try {
            this.logger().notice(String.format("%s - has been triggered", this.getClass().getName()));

            URL remoteUrl = this.handleAction(event);

            // Remote URL could not be created, assume some alert has been set by the sub class to explain why!
            if (remoteUrl == null) {
                return;
            }

            this.logger().notice(String.format("Created remote url '%s'", remoteUrl.toString()));

            if (!this.settings().hasEnabledExtensions()) {
                this.logger().warning("You have no extensions enabled, enable some: Preferences → Open in Git host");
            }

            for (Extension extension: this.container().registeredExtensions()) {
                if (this.settings().extensionIsEnabled(extension)) {
                    this.logger().notice(String.format("Running extension '%s'", extension.displayName()));
                    extension.handle(remoteUrl);
                }
            }
        } catch (OpenInGitHostException exception) {
            this.logger().error(String.format("Could not open file in remote repository ('%s')", exception.getMessage()));
        }
    }

    RemoteUrlFactory remoteUrlFactory()
    {
        return this.container().remoteUrlFactory();
    }

    Settings settings()
    {
        return this.container().settings();
    }

    Logger logger()
    {
        return this.container().logger();
    }

    private Container container()
    {
        if (this.container == null) {
            this.container = ServiceManager.getService(Container.class);
        }

        return this.container;
    }
}
