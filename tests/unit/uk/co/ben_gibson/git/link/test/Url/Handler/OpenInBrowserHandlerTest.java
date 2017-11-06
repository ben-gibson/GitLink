package uk.co.ben_gibson.git.link.test.Url.Handler;

import com.intellij.ide.browsers.BrowserLauncher;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.git.link.Url.Handler.Exception.UrlHandlerException;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;
import uk.co.ben_gibson.git.link.Url.Handler.OpenInBrowserHandler;
import static org.mockito.Mockito.*;
import java.net.MalformedURLException;
import java.net.URL;

@RunWith(DataProviderRunner.class)
public class OpenInBrowserHandlerTest extends TestCase
{
    @Test
    @UseDataProvider("urlProvider")
    public void testOpensInBrowser(URL url, String expected) throws MalformedURLException, UrlHandlerException
    {
        BrowserLauncher browserLauncher = mock(BrowserLauncher.class);

        UrlHandler handler = new OpenInBrowserHandler(browserLauncher);

        handler.handle(url);

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
