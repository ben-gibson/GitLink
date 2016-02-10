package uk.co.ben_gibson.repositorymapper.RepositoryProvider.UrlFactory;

import com.intellij.testFramework.UsefulTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context.ContextTestUtil;
import uk.co.ben_gibson.repositorymapper.UrlFactory.StashUrlFactory;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Tests the Stash Url factory.
 */
@RunWith(Parameterized.class)
public class StashUrlFactoryTest extends UsefulTestCase
{

    private Context context;
    private String expectedUrl;

    /**
     * Constructor.
     *
     * @param context      The context.
     * @param expectedUrl  The expected url to be returned from the context.
     *
     */
    public StashUrlFactoryTest(Context context, String expectedUrl)
    {
        this.context     = context;
        this.expectedUrl = expectedUrl;
    }


    /**
     * Tests the url factory creates the correct url from a given context.
     */
    @Test
    public void testGetUrlFromContext() throws URISyntaxException, RemoteRepositoryMapperException, MalformedURLException
    {
        assertEquals(this.getStashUrlFactory().getUrlFromContext(this.context).toString(), this.expectedUrl);
    }


    /**
     * Acts as a data provider for contexts and their expected url result.
     *
     * @return Collection
     */
    @Parameterized.Parameters
    public static Collection contexts() throws MalformedURLException, RemoteNotFoundException
    {
        return Arrays.asList(new Object[][] {
            {
                ContextTestUtil.getMockedContext("https://stash.example.com/foo/bar", "master", "/src/Bar.java"),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Bar.java?at=refs/heads/master"
            },
            {
                ContextTestUtil.getMockedContext("https://stash.example.com/foo/bar", "foo-bar", "/src/FooBar/Bar.java", 10),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/FooBar/Bar.java?at=refs/heads/foo-bar#10"
            },
            {
                ContextTestUtil.getMockedContext("http://stash.example.com/foo bar/bar", "foo-bar", "/src/Foo Bar/Bar.java", 0),
                "http://stash.example.com/projects/foo%20bar/repos/bar/browse/src/Foo%20Bar/Bar.java?at=refs/heads/foo-bar#0"
            },
        });
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
