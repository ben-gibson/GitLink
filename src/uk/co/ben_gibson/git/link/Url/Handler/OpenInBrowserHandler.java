package uk.co.ben_gibson.git.link.Url.Handler;

import com.intellij.ide.browsers.BrowserLauncher;
import uk.co.ben_gibson.git.link.Url.Handler.Exception.UrlHandlerException;
import java.net.URL;

/**
 * Opens a URL in the default browser.
 */
public class OpenInBrowserHandler implements UrlHandler
{
    private BrowserLauncher browserLauncher;

    public OpenInBrowserHandler(BrowserLauncher browserLauncher)
    {
        this.browserLauncher = browserLauncher;
    }

    public void handle(URL url) throws UrlHandlerException
    {
        try {
            this.browserLauncher.open(url.toURI().toASCIIString());
        } catch (Exception e) {
            throw new UrlHandlerException(e.getMessage());
        }
    }

    public String name()
    {
        return "Open in browser";
    }
}
