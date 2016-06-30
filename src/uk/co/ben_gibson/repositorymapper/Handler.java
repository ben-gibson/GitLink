package uk.co.ben_gibson.repositorymapper;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Host.Host;
import uk.co.ben_gibson.repositorymapper.Host.Url.Exception.ProjectNotFoundException;
import uk.co.ben_gibson.repositorymapper.Host.Url.Exception.UnsupportedHostException;
import uk.co.ben_gibson.repositorymapper.Host.Url.Factory.Factory;
import uk.co.ben_gibson.repositorymapper.Host.Url.Factory.Provider;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Responsible for handling a context.
 */
class Handler
{
    private Provider urlFactoryProvider;

    static Handler getInstance()
    {
        return ServiceManager.getService(Handler.class);
    }

    /**
     * Open the given context in the browser.
     *
     * @param host             The git host to open in.
     * @param context          The context to open.
     * @param forceSSL         Should SSL be enforced.
     * @param copyToClipboard  Should the URL be copied to the clipboard.
     */
    void open(@NotNull Host host, @NotNull Context context, boolean forceSSL, boolean copyToClipboard) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException, UnsupportedHostException, ProjectNotFoundException, RemoteNotFoundException
    {
        Factory factory = this.getUrlFactoryProvider().getForHost(host);

        URL url = factory.createUrl(context, forceSSL);

        if (copyToClipboard) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(url.toString()), null);
        }

        BrowserLauncher.getInstance().open(url.toURI().toString());
    }

    /**
     * Lazy load the url factory provider.
     *
     * @return Provider
     */
    private Provider getUrlFactoryProvider()
    {
        if (this.urlFactoryProvider == null) {
            this.urlFactoryProvider = new Provider();
        }

        return this.urlFactoryProvider;
    }
}
