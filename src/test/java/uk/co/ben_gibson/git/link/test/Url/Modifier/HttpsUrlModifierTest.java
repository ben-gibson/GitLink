package uk.co.ben_gibson.git.link.test.Url.Modifier;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.git.link.Url.Modifier.Exception.ModifierException;
import uk.co.ben_gibson.git.link.Url.Modifier.HttpsUrlModifier;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifier;
import java.net.MalformedURLException;
import java.net.URL;

@RunWith(DataProviderRunner.class)
public class HttpsUrlModifierTest extends TestCase
{
    @Test
    @UseDataProvider("urlProvider")
    public void testForcesHttpsProtocol(URL url, String expected) throws ModifierException
    {
        UrlModifier modifier = new HttpsUrlModifier();

        assertEquals(expected, modifier.modify(url).toString());
    }

    @DataProvider
    public static Object[][] urlProvider() throws MalformedURLException
    {
        return new Object[][] {
            {
                new URL("http://example.com"),
                "https://example.com",
            },
            {
                new URL("https://example.com"),
                "https://example.com",
            },
            {
                new URL("http://github.com/foo%20bar/baz/blob/master/需求0920-2017/0928.sql#L15"),
                "https://github.com/foo%20bar/baz/blob/master/需求0920-2017/0928.sql#L15"
            },
            {
                new URL(                "http://stash.example.com/projects/foo/repos/bar/browse/src/Bar.java?at=refs/heads/master#10"),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Bar.java?at=refs/heads/master#10"
            },
        };
    }
}
