package uk.co.ben_gibson.git.link.Url.Handler;

import com.intellij.ide.browsers.BrowserLauncher;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class BrowserHandler implements UrlHandler
{
    private BrowserLauncher browserLauncher;

    public BrowserHandler()
    {
        this.browserLauncher = BrowserLauncher.getInstance();
    }

    public BrowserHandler(@NotNull BrowserLauncher browserLauncher)
    {
        this.browserLauncher = browserLauncher;
    }


    public void handle(URL url)
    {
        browserLauncher.open(url.toString());
    }
}
