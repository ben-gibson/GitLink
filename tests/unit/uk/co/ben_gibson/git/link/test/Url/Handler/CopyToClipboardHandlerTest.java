package uk.co.ben_gibson.git.link.test.Url.Handler;

import junit.framework.TestCase;
import uk.co.ben_gibson.git.link.Url.Handler.Exception.UrlHandlerException;
import uk.co.ben_gibson.git.link.Url.Handler.CopyToClipboardHandler;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.net.MalformedURLException;
import java.net.URL;
import static org.mockito.Mockito.*;

public class CopyToClipboardHandlerTest extends TestCase
{
    public void testCopiesUrlToClipboard() throws MalformedURLException, UrlHandlerException
    {
        URL url = new URL("https://example.com");

        Toolkit toolkit = mock(Toolkit.class);
        Clipboard clipboard = mock(Clipboard.class);

        when(toolkit.getSystemClipboard()).thenReturn(clipboard);

        UrlHandler handler = new CopyToClipboardHandler(toolkit);

        handler.handle(url);

        verify(clipboard, times(1)).setContents(any(StringSelection.class), isNull());
    }
}
