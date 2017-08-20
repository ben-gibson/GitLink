package uk.co.ben_gibson.open.in.git.host;

import com.intellij.ide.DataManager;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.open.in.git.host.Extension.CopyToClipboardExtension;
import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import uk.co.ben_gibson.open.in.git.host.Extension.OpenInBrowserExtension;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.CompoundRemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.GitHubRemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.Logger.Handlers.DiagnosticLogHandler;
import uk.co.ben_gibson.open.in.git.host.Logger.Handlers.EventLogHandler;
import uk.co.ben_gibson.open.in.git.host.Logger.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Dependency container.
 *
 * There doesn't seem to be much documentation around Intellij's service manager. Services seem to be
 * registered through the plugin.xml but there isn't any information about handling services that have complex dependencies.
 * This acts as a simple alternative.
 */
public class Container
{
    private Plugin plugin;
    private Logger logger;
    private List<Extension> extensions;

    public Project activeProject()
    {
        DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();

        return DataKeys.PROJECT.getData(dataContext);
    }

    public Plugin plugin()
    {
        if (this.plugin == null) {

            PluginId pluginId = PluginId.getId("uk.co.ben-gibson.remote.repository.mapper");

            this.plugin = new Plugin(PluginManager.getPlugin(pluginId));
        }

        return this.plugin;
    }

    public Settings settings()
    {
       return ServiceManager.getService(this.activeProject(), Settings.class);
    }

    public RemoteUrlFactory remoteUrlFactory()
    {
        CompoundRemoteUrlFactory compoundFactory = new CompoundRemoteUrlFactory();

        compoundFactory.registerFactory(new GitHubRemoteUrlFactory(this.settings().forceSSL()));

        return compoundFactory;
    }

    public Logger logger()
    {
        if (this.logger == null) {

            this.logger = new Logger();

            this.logger.registerHandler(new EventLogHandler(this.plugin(), this.settings().enableVerboseEventLog()));

            this.logger.registerHandler(
                new DiagnosticLogHandler(com.intellij.openapi.diagnostic.Logger.getInstance(this.plugin().displayName()))
            );
        }

        return this.logger;
    }

    public List<Extension> registeredExtensions()
    {
        if (this.extensions == null) {
            this.extensions = new ArrayList<>();
            this.extensions.add(new OpenInBrowserExtension());
            this.extensions.add(new CopyToClipboardExtension());
        }

        return this.extensions;
    }

    /**
     * Flushes the lazy loading cache. This is useful when there are have been setting changes with could affect
     * the services behaviour.
     */
    public void flush()
    {
        this.logger = null;
        this.extensions = null;
    }
}
