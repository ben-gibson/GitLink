package uk.co.ben_gibson.git.link;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.components.ServiceManager;
import uk.co.ben_gibson.git.link.Git.RepositoryFactory;
import uk.co.ben_gibson.git.link.UI.ExceptionRenderer;
import uk.co.ben_gibson.git.link.Url.Modifier.HttpsUrlModifier;
import uk.co.ben_gibson.git.link.Url.Handler.CopyToClipboardHandler;
import uk.co.ben_gibson.git.link.Url.Handler.OpenInBrowserHandler;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifierProvider;
import java.awt.*;
import java.util.*;

/**
 * Dependency container.
 *
 * There doesn't seem to be much documentation around Intellij's service manager. Services seem to be
 * registered through the plugin.xml but there isn't any information about handling services that have dependencies.
 */
public class Container
{
    private Hashtable<Class, Object> services = new Hashtable<>();


    public static Container getInstance()
    {
        return ServiceManager.getService(Container.class);
    }


    public Manager manager()
    {
        if (!this.hasService(Manager.class)) {
            this.registerService(
                new Manager(
                    this.repositoryFactory(),
                    this.exceptionRenderer(),
                    this.urlModifierProvider()
                )
            );
        }

        return (Manager) this.service(Manager.class);
    }


    public Plugin plugin()
    {
        if (!this.hasService(Plugin.class)) {
            this.registerService(Plugin.createDefault());
        }

        return (Plugin) this.service(Plugin.class);
    }


    public ExceptionRenderer exceptionRenderer()
    {
        if (!this.hasService(ExceptionRenderer.class)) {
            this.registerService(new ExceptionRenderer(this.plugin()));
        }

        return (ExceptionRenderer) this.service(ExceptionRenderer.class);
    }


    public OpenInBrowserHandler openInBrowserHandler()
    {
        if (!this.hasService(OpenInBrowserHandler.class)) {
            this.registerService(new OpenInBrowserHandler(BrowserLauncher.getInstance()));
        }

        return (OpenInBrowserHandler) this.service(OpenInBrowserHandler.class);
    }


    public CopyToClipboardHandler copyToClipboardHandler()
    {
        if (!this.hasService(CopyToClipboardHandler.class)) {
            this.registerService(new CopyToClipboardHandler(Toolkit.getDefaultToolkit()));
        }

        return (CopyToClipboardHandler) this.service(CopyToClipboardHandler.class);
    }


    public UrlModifierProvider urlModifierProvider()
    {
        if (!this.hasService(UrlModifierProvider.class)) {

            UrlModifierProvider provider = new UrlModifierProvider(
                Collections.singletonList(
                    new HttpsUrlModifier()
                )
            );

            this.registerService(provider);
        }

        return (UrlModifierProvider) this.service(UrlModifierProvider.class);
    }


    public RepositoryFactory repositoryFactory()
    {
        if (!this.hasService(RepositoryFactory.class)) {
            this.registerService(new RepositoryFactory());
        }

        return (RepositoryFactory)this.service(RepositoryFactory.class);
    }


    private void registerService(Object service)
    {
        this.services.put(service.getClass(), service);
    }


    private Object service(Class serviceClass)
    {
        return this.services.get(serviceClass);
    }


    private Boolean hasService(Class serviceClass)
    {
        return this.services.containsKey(serviceClass);
    }
}
