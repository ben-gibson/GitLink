package uk.co.ben_gibson.repositorymapper;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import uk.co.ben_gibson.repositorymapper.Logger.Handlers.DiagnosticLogHandler;
import uk.co.ben_gibson.repositorymapper.Logger.Handlers.EventLogHandler;
import uk.co.ben_gibson.repositorymapper.Logger.Logger;
import uk.co.ben_gibson.repositorymapper.Plugin.Plugin;

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
     * Get the plugin settings scoped to the given project.
     */
    public Settings getSettings()
    {
        if (this.settings == null) {
            this.settings = new Settings(true);
        }

        return this.settings;
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
