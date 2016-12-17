package uk.co.ben_gibson.repositorymapper.RepositoryProvider.UrlFactory;

import com.intellij.testFramework.UsefulTestCase;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context.ContextTestUtil;
import uk.co.ben_gibson.repositorymapper.UrlFactory.Exception.ProjectNotFoundException;
import uk.co.ben_gibson.repositorymapper.UrlFactory.StashUrlFactory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Tests the stash url factory.
 */
@RunWith(DataProviderRunner.class)
public class StashUrlFactoryTest extends UsefulTestCase
{


    /**
     * Tests the url factory creates the correct url for a context.
     */
    @Test
    @UseDataProvider("getContexts")
    public void testGetUrlFromContext(Context context, String expectedUrl) throws ProjectNotFoundException, MalformedURLException, RemoteNotFoundException, URISyntaxException
    {
        assertEquals(expectedUrl, this.getStashUrlFactory().createUrl(context, false).toString());
    }


    /**
     * Tests the project not found exception is thrown when no project is included in remote url
     */
    @Test(expected=ProjectNotFoundException.class)
    public void testGetUrlFromContext() throws URISyntaxException, MalformedURLException, RemoteNotFoundException, ProjectNotFoundException
    {
        Context context = ContextTestUtil.getMockedContext("https://stash.example.com", "master", "", "Bar.java", null, null);
        this.getStashUrlFactory().createUrl(context, false);
    }


    /**
     * Acts as a data provider for contexts.
     *
     * @return  Object[][]
     */
    @DataProvider
    public static Object[][] getContexts() throws MalformedURLException, RemoteNotFoundException
    {
        return new Object[][] {
            {
                ContextTestUtil.getMockedContext("https://stash.example.com/foo/bar", "master", "/src/Bar.java", "Bar.java", null, null),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Bar.java?at=refs/heads/master"
            },
            {
                ContextTestUtil.getMockedContext("https://stash.example.com/foo/bar", "foo-bar", "/src/FooBar/Bar.java", "Bar.java", null, 10),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/FooBar/Bar.java?at=refs/heads/foo-bar#10"
            },
            {
                ContextTestUtil.getMockedContext("http://stash.example.com/foo bar/bar", "foo-bar", "/src/Foo Bar/Bar.java", "Bar.java", null, 0),
                "http://stash.example.com/projects/foo%20bar/repos/bar/browse/src/Foo%20Bar/Bar.java?at=refs/heads/foo-bar#0"
            },
            {
                ContextTestUtil.getMockedContext("http://stash.example.com/foo bar/bar", "foo-bar", "/src/Foo Bar/Bar.java", "Bar.java", "ab342nfj2324", 10),
                "http://stash.example.com/projects/foo%20bar/repos/bar/commits/ab342nfj2324"
            },
            {
                ContextTestUtil.getMockedContext("http://stash.example.com/foo bar/bar", "foo-bar", "/src/Foo Bar/Bar.java", "Bar.java", "ab342n", null),
                "http://stash.example.com/projects/foo%20bar/repos/bar/commits/ab342n"
            },
        };
    }


    /**
     * Get the url factory.
     *
     * @return StashUrlFactory
     */
    public StashUrlFactory getStashUrlFactory()
    {
        return new StashUrlFactory();
    }
}
