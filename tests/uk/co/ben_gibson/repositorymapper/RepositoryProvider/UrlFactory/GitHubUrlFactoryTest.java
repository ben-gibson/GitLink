package uk.co.ben_gibson.repositorymapper.RepositoryProvider.UrlFactory;

import com.intellij.testFramework.UsefulTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context.ContextTestUtil;
import uk.co.ben_gibson.repositorymapper.UrlFactory.GitHubUrlFactory;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Tests the GitHub Url factory.
 */
@RunWith(Parameterized.class)
public class GitHubUrlFactoryTest extends UsefulTestCase
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
    public GitHubUrlFactoryTest(Context context, String expectedUrl)
    {
        this.context     = context;
        this.expectedUrl = expectedUrl;
    }


    /**
     * Tests the url factory creates the correct url from a given context.
     */
    @Test
    public void testGetUrlFromContext() throws URISyntaxException, RemoteRepositoryMapperException, MalformedURLException, UnsupportedEncodingException
    {
        assertEquals(this.expectedUrl, this.getGitHubUrlFactory().getUrlFromContext(this.context).toString());
    }


    /**
     * Acts as a data provider for contexts and their expected url result.
     *
     * @return Collection
     */
    @Parameterized.Parameters
    public static Collection contexts() throws MalformedURLException, RemoteRepositoryMapperException
    {
        return Arrays.asList(new Object[][] {
            {
                ContextTestUtil.getMockedContext("https://github.com/foo/bar", "master", "/src/Bar.java"),
                "https://github.com/foo/bar/blob/master/src/Bar.java"
            },
            {
                ContextTestUtil.getMockedContext("https://github.com/foo/bar", "foo-bar", "/src/FooBar/Bar.java", 10),
                "https://github.com/foo/bar/blob/foo-bar/src/FooBar/Bar.java#L10"
            },
            {
                ContextTestUtil.getMockedContext("https://github.com/foo bar/bar", "misc/foo-bar", "/src/Foo Bar/Bar.java", 0),
                "https://github.com/foo%20bar/bar/blob/misc%252Ffoo-bar/src/Foo%20Bar/Bar.java#L0"
            },
        });
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
