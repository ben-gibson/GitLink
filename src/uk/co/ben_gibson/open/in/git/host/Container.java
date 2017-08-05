package uk.co.ben_gibson.open.in.git.host;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory.CompoundRemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory.GitHubRemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.UrlFactory.RemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.Logger.Handlers.DiagnosticLogHandler;
import uk.co.ben_gibson.open.in.git.host.Logger.Handlers.EventLogHandler;
import uk.co.ben_gibson.open.in.git.host.Logger.Logger;

/**
 * Dependency container.
 *
 * Wraps the Intellij service manager. I'm unsure if Intellij have a better solution to dependency injection or the Java
 * world in general. Coming from a purely PHP background I have opted for this simple container.
 */
public class Container
{
    private Plugin plugin;
    private Logger logger;
    private Settings settings;

    /**
     * Get the plugin.
     */
    public Plugin getPlugin()
    {
        if (this.plugin == null) {

            PluginId pluginId = PluginId.getId("uk.co.ben-gibson.remote.repository.mapper");

            this.plugin = new Plugin(PluginManager.getPlugin(pluginId));
        }

        return this.plugin;
    }

    /**
     * Get the remote host configured in the settings.
     */
    public RemoteHost getRemoteHost()
    {
        return this.getSettings().getRemoteHost();
    }

    /**
     * Get the plugin settings scoped to the given project.
     */
    public Settings getSettings()
    {
        if (this.settings == null) {
            this.settings = new Settings(RemoteHost.GIT_HUB, true, true);
        }

        return this.settings;
    }

    /**
     * Get the remote url factory.
     */
    public RemoteUrlFactory getRemoteUrlFactory()
    {
        boolean forceSSL = this.getSettings().forceSSL();

        CompoundRemoteUrlFactory compoundFactory = new CompoundRemoteUrlFactory();

        compoundFactory.registerFactory(new GitHubRemoteUrlFactory(forceSSL));

        return compoundFactory;
    }

    /**
     * Get the logger.
     */
    public Logger getLogger()
    {
        if (this.logger == null) {

            this.logger = new Logger();

            if (this.getSettings().enableEventLog()) {
                this.logger.registerHandler(new EventLogHandler(this.getPlugin()));
            }

            this.logger.registerHandler(
                new DiagnosticLogHandler(com.intellij.openapi.diagnostic.Logger.getInstance(this.getPlugin().getName()))
            );
        }

        return this.logger;
    }
}
