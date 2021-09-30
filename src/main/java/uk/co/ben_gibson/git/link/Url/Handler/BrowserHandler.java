package uk.co.ben_gibson.git.link.Url.Handler;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.serviceContainer.NonInjectable;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class BrowserHandler implements UrlHandler
{
    private BrowserLauncher browserLauncher;

    public BrowserHandler() {
        this.browserLauncher = BrowserLauncher.getInstance();
    }

    @NonInjectable
    public BrowserHandler(@NotNull BrowserLauncher browserLauncher) {
        this.browserLauncher = browserLauncher;
    }

    public static BrowserHandler getInstance() {
        return ApplicationManager.getApplication().getService(BrowserHandler.class);
    }

    @Override
    public void handle(URL url) {
        browserLauncher.open(url.toString());
    }
}
