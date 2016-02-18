package uk.co.ben_gibson.repositorymapper.RepositoryProvider.UrlFactory;

import com.intellij.testFramework.UsefulTestCase;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context.ContextTestUtil;
import uk.co.ben_gibson.repositorymapper.UrlFactory.GitHubUrlFactory;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Tests the GitHub Url factory.
 */
@RunWith(DataProviderRunner.class)
public class GitHubUrlFactoryTest extends UsefulTestCase
{


    /**
     * Tests the url factory creates the correct url for a context.
     */
    @Test
    @UseDataProvider("getContexts")
    public void testGetUrlFromContext(Context context, String expectedUrl) throws URISyntaxException, RemoteRepositoryMapperException, MalformedURLException, UnsupportedEncodingException
    {
        assertEquals(expectedUrl, this.getGitHubUrlFactory().getUrlFromContext(context).toString());
    }


    /**
     * Acts as a data provider for contexts.
     *
     * @return Object[][]
     */
    @DataProvider
    public static  Object[][] getContexts() throws MalformedURLException, RemoteRepositoryMapperException
    {
        return new Object[][] {
            {
                ContextTestUtil.getMockedContext("https://github.com/foo/bar", "master", "/src/Bar.java", "Bar.java", null),
                "https://github.com/foo/bar/blob/master/src/Bar.java"
            },
            {
                ContextTestUtil.getMockedContext("https://github.com/foo/bar", "foo-bar", "/src/FooBar/Bar.java", "Bar.java", 10),
                "https://github.com/foo/bar/blob/foo-bar/src/FooBar/Bar.java#L10"
            },
            {
                ContextTestUtil.getMockedContext("https://github.com/foo bar/bar", "misc/foo-bar", "/src/Foo Bar/Bar.java", "Bar.java", 0),
                "https://github.com/foo%20bar/bar/blob/misc%252Ffoo-bar/src/Foo%20Bar/Bar.java#L0"
            },
        };
    }


    /**
     * Get the url factory.
     *
     * @return GitHubUrlFactory
     */
    public GitHubUrlFactory getGitHubUrlFactory()
    {
        return new GitHubUrlFactory();
    }
}
