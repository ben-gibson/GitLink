package uk.co.ben_gibson.open.in.git.host.test.Extension;

import com.intellij.ide.browsers.BrowserLauncher;
import junit.framework.TestCase;
import uk.co.ben_gibson.open.in.git.host.Extension.Exception.ExtensionException;
import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import uk.co.ben_gibson.open.in.git.host.Extension.OpenInBrowserExtension;
import static org.mockito.Mockito.*;
import java.net.MalformedURLException;
import java.net.URL;

public class OpenInBrowserExtensionTest extends TestCase
{
    public void testOpensInBrowser() throws MalformedURLException, ExtensionException
    {
        URL url = new URL("https://example.com");

        BrowserLauncher browserLauncher = mock(BrowserLauncher.class);

        Extension extension = new OpenInBrowserExtension(browserLauncher);

        extension.run(url);

        verify(browserLauncher, times(1)).open(url.toString());
    }
}
