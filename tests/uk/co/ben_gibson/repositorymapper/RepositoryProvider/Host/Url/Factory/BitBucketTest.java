package uk.co.ben_gibson.repositorymapper.RepositoryProvider.Host.Url.Factory;

import com.intellij.testFramework.UsefulTestCase;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context.ContextTestUtil;
import uk.co.ben_gibson.repositorymapper.Host.Url.Factory.BitBucket;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Tests the BitBucket url factory.
 */
@RunWith(DataProviderRunner.class)
public class BitBucketTest extends UsefulTestCase
{


    /**
     * Tests the url factory creates the correct url for a context.
     */
    @Test
    @UseDataProvider("getContexts")
    public void testGetUrlFromContext(Context context, String expectedUrl) throws URISyntaxException, RemoteNotFoundException, MalformedURLException
    {
        assertEquals(expectedUrl, this.getBitBucketUrlFactory().createUrl(context, false).toString());
    }


    /**
     * Acts as a data provider for contexts.
     *
     * @return Object[][]
     */
    @DataProvider
    public static Object[][] getContexts() throws MalformedURLException, RemoteNotFoundException
    {
        return new Object[][] {
            {
                ContextTestUtil.getMockedContext("https://bitbucket.org/foo/bar", "master", "/src/Bar.java", "Bar.java", null),
                "https://bitbucket.org/foo/bar/src/HEAD/src/Bar.java?at=master"
            },
            {
                ContextTestUtil.getMockedContext("https://bitbucket.org/foo/bar", "foo-bar", "/src/FooBar/Bar.java", "Bar.java", 10),
                "https://bitbucket.org/foo/bar/src/HEAD/src/FooBar/Bar.java?at=foo-bar#Bar.java-10"
            },
            {
                ContextTestUtil.getMockedContext("https://bitbucket.org/foo bar/bar", "misc/foo-bar", "/src/Foo Bar/Bar.java", "Bar.java", 0),
                "https://bitbucket.org/foo%20bar/bar/src/HEAD/src/Foo%20Bar/Bar.java?at=misc/foo-bar#Bar.java-0"
            },
            {
                ContextTestUtil.getMockedContext("https://example@bitbucket.org/foo bar/bar", "misc/foo-bar", "/src/Foo Bar/Bar.java", "Bar.java", 0),
                "https://bitbucket.org/foo%20bar/bar/src/HEAD/src/Foo%20Bar/Bar.java?at=misc/foo-bar#Bar.java-0"
            },
        };
    }


    /**
     * Get the url factory.
     *
     * @return BitBucket
     */
    public BitBucket getBitBucketUrlFactory()
    {
        return new BitBucket();
    }
}
