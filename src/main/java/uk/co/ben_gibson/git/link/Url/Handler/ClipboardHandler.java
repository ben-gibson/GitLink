package uk.co.ben_gibson.git.link.Url.Handler;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.serviceContainer.NonInjectable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;

public class ClipboardHandler implements UrlHandler
{
    private Toolkit toolkit;

    public ClipboardHandler() {
        this.toolkit = Toolkit.getDefaultToolkit();
    }

    @NonInjectable
    public ClipboardHandler(@NotNull Toolkit toolkit) {
        this.toolkit = toolkit;
    }

    public static ClipboardHandler getInstance() {
        return ServiceManager.getService(ClipboardHandler.class);
    }

    @Override
    public void handle(URL url) {
        this.toolkit.getSystemClipboard().setContents(new StringSelection(url.toString()), null);
    }
}
