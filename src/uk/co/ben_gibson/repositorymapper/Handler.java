package uk.co.ben_gibson.repositorymapper;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Host.Host;
import uk.co.ben_gibson.repositorymapper.UrlFactory.UrlFactory;
import uk.co.ben_gibson.repositorymapper.UrlFactory.UrlFactoryProvider;
import uk.co.ben_gibson.repositorymapper.Notification.Notifier;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

/**
 * Responsible for handling a context.
 */
class Handler
{
    private static final String INSTALL_ID_KEY = "uk.co.ben-gibson.remote.repository.mapper.install.id";
    private static final com.intellij.openapi.diagnostic.Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(Handler.class.getName());

    private UrlFactoryProvider urlFactoryProvider;

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
    void open(@NotNull final Host host, @NotNull final Context context, final boolean forceSSL, final boolean copyToClipboard)
    {
        Task.Backgroundable task = new Task.Backgroundable(null, "Opening In Git Host") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    UrlFactory factory = Handler.this.getUrlFactoryProvider().getForHost(host);

                    URL url = factory.createUrl(context, forceSSL);

                    if (copyToClipboard) {
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(url.toString()), null);
                    }

                    BrowserLauncher.getInstance().open(url.toURI().toString());

                    IdeaPluginDescriptor plugin = PluginManager.getPlugin(PluginId.getId("uk.co.ben-gibson.remote.repository.mapper"));

                    if (plugin == null) {
                        LOG.info("Could not find plugin, skipping analytics.");
                        return;
                    }

                    URI googleAnalyticsUrl = new URI(
                        "https",
                        "google-analytics.com",
                        "/collect",
                        String.format(
                            "v=%s&tid=%s&cid=%s&t=event&an=%s&aid=%s&av=%s&ec=%s&ea=open&el=%s",
                            1,
                            "UA-89097365-1",
                            getInstallId(),
                            "Open in git host",
                            plugin.getPluginId(),
                            plugin.getVersion(),
                            host.toString(),
                            url.toURI().toString()
                        ),
                        null
                    );

                    InputStream stream = googleAnalyticsUrl.toURL().openStream();
                    stream.close();

                }  catch (Exception e) {
                    Notifier.errorNotification(e.getMessage());
                }
            }
        };

        ProgressManager.getInstance().run(task);
    }

    /**
     * Lazy load the url factory provider.
     *
     * @return UrlFactoryProvider
     */
    private UrlFactoryProvider getUrlFactoryProvider()
    {
        if (this.urlFactoryProvider == null) {
            this.urlFactoryProvider = new UrlFactoryProvider();
        }

        return this.urlFactoryProvider;
    }

    /**
     * Get the install id.
     *
     * @return String
     */
    private String getInstallId()
    {
        String installId = PropertiesComponent.getInstance().getValue(INSTALL_ID_KEY);

        if (installId == null) {
            installId = UUID.randomUUID().toString();
            LOG.info(String.format("Creating install id '%s'", installId));
            PropertiesComponent.getInstance().setValue(INSTALL_ID_KEY, installId);
        }

        return installId;
    }
}
