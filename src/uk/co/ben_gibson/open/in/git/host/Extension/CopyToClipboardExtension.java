package uk.co.ben_gibson.open.in.git.host.Extension;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;

/**
 * An extension to copy a remote git url to the clipboard.
 */
public class CopyToClipboardExtension implements Extension
{
    private Toolkit toolkit;

    public CopyToClipboardExtension(Toolkit toolkit)
    {
        this.toolkit = toolkit;
    }

    public void run(URL remoteUrl)
    {
        this.toolkit.getSystemClipboard().setContents(new StringSelection(remoteUrl.toString()), null);
    }

    public String displayName()
    {
        return "Copy to Clipboard";
    }
}
