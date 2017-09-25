package uk.co.ben_gibson.open.in.git.host;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.open.in.git.host.Extension.CopyToClipboardExtension;
import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import uk.co.ben_gibson.open.in.git.host.Extension.ExtensionRunner;
import uk.co.ben_gibson.open.in.git.host.Extension.OpenInBrowserExtension;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.*;
import uk.co.ben_gibson.open.in.git.host.Logger.Handlers.DiagnosticLogHandler;
import uk.co.ben_gibson.open.in.git.host.Logger.Handlers.EventLogHandler;
import uk.co.ben_gibson.open.in.git.host.Logger.Logger;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import java.awt.*;
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
    private List<Extension> extensions;
    private RemoteUrlFactoryProvider remoteUrlFactoryProvider;

    public Plugin plugin()
    {
        if (this.plugin == null) {

            PluginId pluginId = PluginId.getId("uk.co.ben-gibson.remote.repository.mapper");

            this.plugin = new Plugin(PluginManager.getPlugin(pluginId));
        }

        return this.plugin;
    }

    public Settings settings(Project project)
    {
       return ServiceManager.getService(project, Settings.class);
    }

    public RemoteUrlFactoryProvider remoteUrlFactoryProvider()
    {
        if (this.remoteUrlFactoryProvider == null) {

            this.remoteUrlFactoryProvider = new RemoteUrlFactoryProvider();

            this.remoteUrlFactoryProvider.registerFactory(new GitHubRemoteUrlFactory());
            this.remoteUrlFactoryProvider.registerFactory(new BitBucketRemoteUrlFactory());
            this.remoteUrlFactoryProvider.registerFactory(new StashRemoteUrlFactory());
        }

        return this.remoteUrlFactoryProvider;
    }

    public RemoteUrlFactory remoteUrlFactory(Project project) throws RemoteUrlFactoryException
    {
        return this.remoteUrlFactoryProvider().remoteUrlFactoryForHost(this.remoteHost(project));
    }

    public RemoteHost remoteHost(Project project)
    {
        return this.settings(project).getRemoteHost();
    }

    public Logger logger(Project project)
    {
        Logger logger = new Logger();

        logger.registerHandler(new EventLogHandler(this.plugin(), this.settings(project).getEnableVerboseEventLog()));

        logger.registerHandler(
            new DiagnosticLogHandler(com.intellij.openapi.diagnostic.Logger.getInstance(this.plugin().displayName()))
        );

        return logger;
    }

    public List<Extension> registeredExtensions()
    {
        if (this.extensions == null) {
            this.extensions = new ArrayList<>();
            this.extensions.add(new OpenInBrowserExtension(BrowserLauncher.getInstance()));
            this.extensions.add(new CopyToClipboardExtension(Toolkit.getDefaultToolkit()));
        }

        return this.extensions;
    }

    public List<Extension> enabledExtensions(Project project)
    {
        Settings settings = this.settings(project);

        List<Extension> enabledExtensions = new ArrayList<>();

        for (Extension extension: this.registeredExtensions()) {
            if (settings.isExtensionEnabled(extension)) {
                enabledExtensions.add(extension);
            }
        }

        return enabledExtensions;
    }

    public ExtensionRunner extensionRunner(Project project)
    {
        return new ExtensionRunner(this.logger(project), this.enabledExtensions(project));
    }
}
