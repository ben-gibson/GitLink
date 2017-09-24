package uk.co.ben_gibson.open.in.git.host.Extension;

import com.intellij.ide.browsers.BrowserLauncher;
import uk.co.ben_gibson.open.in.git.host.Extension.Exception.ExtensionException;
import java.net.URL;

/**
 * An extension to open a remote git url in the browser.
 */
public class OpenInBrowserExtension implements Extension
{
    private BrowserLauncher browserLauncher;

    public OpenInBrowserExtension(BrowserLauncher browserLauncher)
    {
        this.browserLauncher = browserLauncher;
    }

    public void run(URL remoteUrl) throws ExtensionException
    {
        this.browserLauncher.open(remoteUrl.toString());
    }

    public String displayName()
    {
        return "Open in Browser";
    }
}
