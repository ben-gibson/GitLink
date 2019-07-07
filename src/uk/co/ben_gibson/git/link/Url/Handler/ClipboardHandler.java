package uk.co.ben_gibson.git.link.Url.Handler;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;

public class ClipboardHandler implements UrlHandler
{
    private Toolkit toolkit;

    public ClipboardHandler()
    {
        this.toolkit = Toolkit.getDefaultToolkit();
    }


    public ClipboardHandler(@NotNull Toolkit toolkit)
    {
        this.toolkit = toolkit;
    }


    public void handle(URL url)
    {
        this.toolkit.getSystemClipboard().setContents(new StringSelection(url.toString()), null);
    }
}
