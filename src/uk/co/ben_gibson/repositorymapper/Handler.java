package uk.co.ben_gibson.repositorymapper;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.components.ServiceManager;
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
import java.net.URL;
import java.util.UUID;

/**
 * Responsible for handling a context.
 */
class Handler
{
    private UrlFactoryProvider urlFactoryProvider;
    private UUID installId = UUID.randomUUID();

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

                    URL googleAnalyticsUrl = new URL(String.format(
                        "https://google-analytics.com/collect?v=%s&tid=%s&cid=%s&t=event&ec=%s&ea=open&el=%sl",
                        1,
                        "UA-89097365-1",
                        installId.toString(),
                        host.toString(),
                        url.toURI().toString()
                    ));

                    InputStream stream = googleAnalyticsUrl.openStream();
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
}
