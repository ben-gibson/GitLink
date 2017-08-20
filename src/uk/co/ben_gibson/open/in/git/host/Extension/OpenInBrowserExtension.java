package uk.co.ben_gibson.open.in.git.host.Extension;

import com.intellij.ide.browsers.BrowserLauncher;
import uk.co.ben_gibson.open.in.git.host.Extension.Exception.ExtensionException;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * An extension to open a remote git url in the browser.
 */
public class OpenInBrowserExtension implements Extension
{
    public void handle(URL remoteUrl) throws ExtensionException
    {
        try {
            BrowserLauncher.getInstance().open(remoteUrl.toURI().toString());
        } catch (URISyntaxException e) {
            throw ExtensionException.couldNotHandleRemoteUrl(this, remoteUrl);
        }
    }

    public String displayName()
    {
        return "Open in Browser";
    }
}
