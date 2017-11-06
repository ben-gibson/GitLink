package uk.co.ben_gibson.git.link;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.git.link.Git.RepositoryFactory;
import uk.co.ben_gibson.git.link.Logger.Handlers.DiagnosticLogHandler;
import uk.co.ben_gibson.git.link.Logger.Logger;
import uk.co.ben_gibson.git.link.Url.Modifier.HttpsUrlModifier;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifier;
import uk.co.ben_gibson.git.link.Url.Factory.*;
import uk.co.ben_gibson.git.link.Logger.Handlers.EventLogHandler;
import uk.co.ben_gibson.git.link.Url.Handler.CopyToClipboardHandler;
import uk.co.ben_gibson.git.link.Url.Handler.OpenInBrowserHandler;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
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
    private Hashtable<Class, Object> applicationServices;
    private Hashtable<Project, Hashtable<Class, Object>> projectServices;

    public Container()
    {
        this.applicationServices = new Hashtable<>();
        this.projectServices     = new Hashtable<>();
    }

    /**
     * If the plugins project configuration changes we need to flush the lazy load cache.
     */
    public void flushProjectCache(Project project)
    {
        this.projectServices.remove(project);
    }

    public Plugin plugin()
    {
        if (!this.hasApplicationService(Plugin.class)) {
            this.registerApplicationService(
                new Plugin(PluginManager.getPlugin(PluginId.getId("uk.co.ben-gibson.remote.repository.mapper")))
            );
        }

        return (Plugin)this.applicationService(Plugin.class);
    }

    public Preferences preferences(Project project)
    {
       return ServiceManager.getService(project, Preferences.class);
    }

    public UrlFactoryProvider urlFactoryProvider(Project project)
    {
        if (!this.hasProjectService(project, UrlFactoryProvider.class)) {

            Preferences preferences = this.preferences(project);

            UrlFactoryProvider provider = new UrlFactoryProvider();

            provider.registerFactory(new GitHubUrlFactory());
            provider.registerFactory(new BitBucketUrlFactory());
            provider.registerFactory(new StashUrlFactory());
            provider.registerFactory(
                new CustomUrlFactory(preferences.getCustomFileUrlTemplate(), preferences.getCustomCommitUrlTemplate())
            );

            this.registerProjectService(project, provider);
        }

        return (UrlFactoryProvider)this.projectService(project, UrlFactoryProvider.class);
    }

    public OpenInBrowserHandler openInBrowserHandler()
    {
        if (!this.hasApplicationService(OpenInBrowserHandler.class)) {
            this.registerApplicationService(new OpenInBrowserHandler(BrowserLauncher.getInstance()));
        }

        return (OpenInBrowserHandler)this.applicationService(OpenInBrowserHandler.class);
    }

    public CopyToClipboardHandler copyToClipboardHandler()
    {
        if (!this.hasApplicationService(CopyToClipboardHandler.class)) {
            this.registerApplicationService(new CopyToClipboardHandler(Toolkit.getDefaultToolkit()));
        }

        return (CopyToClipboardHandler)this.applicationService(CopyToClipboardHandler.class);
    }

    public Logger logger(Project project)
    {
        if (!this.hasProjectService(project, Logger.class)) {

            Logger logger = new Logger();

            logger.registerHandler(new EventLogHandler(this.plugin(), this.preferences(project).getEnableVerboseEventLog()));

            logger.registerHandler(
                new DiagnosticLogHandler(com.intellij.openapi.diagnostic.Logger.getInstance(this.plugin().displayName()))
            );

            this.registerProjectService(project, logger);
        }

        return (Logger)this.projectService(project, Logger.class);
    }

    public List<UrlModifier> urlModifiers()
    {
        if (!this.hasApplicationService(UrlModifier.class)) {

            List<UrlModifier> modifiers = new ArrayList<>();

            modifiers.add(new HttpsUrlModifier());

            this.applicationServices.put(UrlModifier.class, modifiers);
        }

        return (List<UrlModifier>)this.applicationService(UrlModifier.class);
    }

    public Runner runner()
    {
        if (!this.hasApplicationService(Runner.class)) {
            this.registerApplicationService(new Runner());
        }

        return (Runner)this.applicationService(Runner.class);
    }

    public RepositoryFactory repositoryFactory()
    {
        if (!this.hasApplicationService(RepositoryFactory.class)) {
            this.registerApplicationService(new RepositoryFactory());
        }

        return (RepositoryFactory)this.applicationService(RepositoryFactory.class);
    }

    private void registerApplicationService(Object service)
    {
        this.applicationServices.put(service.getClass(), service);
    }

    private Object applicationService(Class serviceClass)
    {
        return this.applicationServices.get(serviceClass);
    }

    private Boolean hasApplicationService(Class serviceClass)
    {
        return this.applicationServices.containsKey(serviceClass);
    }

    private void registerProjectService(Project project, Object service)
    {
        if (!this.projectServices.containsKey(project)) {
            this.projectServices.put(project, new Hashtable<>());
        }

        this.projectServices.get(project).put(service.getClass(), service);
    }

    private Object projectService(Project project, Class serviceClass)
    {
        if (!this.projectServices.containsKey(project)) {
            return null;
        }

        return this.projectServices.get(project).get(serviceClass);
    }

    private Boolean hasProjectService(Project project, Class serviceClass)
    {
        return this.projectServices.containsKey(project) && this.projectServices.get(project).containsKey(serviceClass);
    }
}
