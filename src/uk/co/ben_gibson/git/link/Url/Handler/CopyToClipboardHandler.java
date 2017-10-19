package uk.co.ben_gibson.git.link.Url.Handler;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;

/**
 * Copies a URL to the clipboard.
 */
public class CopyToClipboardHandler implements UrlHandler
{
    private Toolkit toolkit;

    public CopyToClipboardHandler(Toolkit toolkit)
    {
        this.toolkit = toolkit;
    }

    public void handle(URL url)
    {
        this.toolkit.getSystemClipboard().setContents(new StringSelection(url.toString()), null);
    }

    public String name()
    {
        return "Copy to clipboard";
    }
}
