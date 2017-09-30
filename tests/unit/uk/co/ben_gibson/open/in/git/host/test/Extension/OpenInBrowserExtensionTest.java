package uk.co.ben_gibson.open.in.git.host.test.Extension;

import com.intellij.ide.browsers.BrowserLauncher;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.open.in.git.host.Extension.Exception.ExtensionException;
import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import uk.co.ben_gibson.open.in.git.host.Extension.OpenInBrowserExtension;
import static org.mockito.Mockito.*;
import java.net.MalformedURLException;
import java.net.URL;

@RunWith(DataProviderRunner.class)
public class OpenInBrowserExtensionTest extends TestCase
{
    @Test
    @UseDataProvider("urlProvider")
    public void testOpensInBrowser(URL url, String expected) throws MalformedURLException, ExtensionException
    {
        BrowserLauncher browserLauncher = mock(BrowserLauncher.class);

        Extension extension = new OpenInBrowserExtension(browserLauncher);

        extension.run(url);

        verify(browserLauncher, times(1)).open(expected);
    }

    @DataProvider
    public static Object[][] urlProvider() throws MalformedURLException
    {
        return new Object[][] {
            {
                new URL("https://example.com"),
                "https://example.com"
            },
            {
                new URL("https://github.com/foo%20bar/baz/blob/master/需求0920-2017/0928.sql#L15"),
                "https://github.com/foo%20bar/baz/blob/master/%E9%9C%80%E6%B1%820920-2017/0928.sql#L15"
            },
        };
    }
}
