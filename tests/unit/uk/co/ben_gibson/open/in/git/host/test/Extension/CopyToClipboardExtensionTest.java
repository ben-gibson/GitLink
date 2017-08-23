package uk.co.ben_gibson.open.in.git.host.test.Extension;

import junit.framework.TestCase;
import uk.co.ben_gibson.open.in.git.host.Extension.CopyToClipboardExtension;
import uk.co.ben_gibson.open.in.git.host.Extension.Exception.ExtensionException;
import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.MalformedURLException;
import java.net.URL;
import static org.mockito.Mockito.*;

public class CopyToClipboardExtensionTest extends TestCase
{
    public void testCopiesUrlToClipboard() throws MalformedURLException, ExtensionException
    {
        URL url = new URL("https://example.com");

        Toolkit toolkit = mock(Toolkit.class);
        Clipboard clipboard = mock(Clipboard.class);

        when(toolkit.getSystemClipboard()).thenReturn(clipboard);

        Extension extension = new CopyToClipboardExtension(toolkit);

        extension.run(url);

        verify(clipboard, times(1)).setContents(any(StringSelection.class), isNull());
    }
}
