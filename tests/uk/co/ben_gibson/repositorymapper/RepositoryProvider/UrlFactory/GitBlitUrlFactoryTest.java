package uk.co.ben_gibson.repositorymapper.RepositoryProvider.UrlFactory;

import com.intellij.testFramework.UsefulTestCase;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.UrlFactory.Exception.ProjectNotFoundException;
import uk.co.ben_gibson.repositorymapper.UrlFactory.GitBlitUrlFactory;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context.ContextTestUtil;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Tests the GitBlit url factory.
 */
@RunWith(DataProviderRunner.class)
public class GitBlitUrlFactoryTest extends UsefulTestCase {

    /**
     * Tests the url factory creates the correct url for a context.
     */
    @Test
    @UseDataProvider("getContexts")
    public void testGetUrlFromContext(Context context, String expectedUrl) throws MalformedURLException, UnsupportedEncodingException, RemoteNotFoundException, URISyntaxException, ProjectNotFoundException {
        assertEquals(expectedUrl, this.getGitBlitUrlFactory().createUrl(context, false).toString());
    }

    /**
     * Tests the url factory creates the correct url for a context.
     */
    @Test
    @UseDataProvider("getContextsForSSL")
    public void testGetUrlFromContextForcedSSL(Context context, String expectedUrl) throws MalformedURLException, UnsupportedEncodingException, RemoteNotFoundException, URISyntaxException, ProjectNotFoundException {
        assertEquals(expectedUrl, this.getGitBlitUrlFactory().createUrl(context, true).toString());
    }

    /**
     * Acts as a data provider for contexts.
     *
     * @return Object[][]
     */
    @DataProvider
    public static Object[][] getContexts() throws MalformedURLException, RemoteNotFoundException {


        return new Object[][]{
                {
                        ContextTestUtil.getMockedContext(
                                "http://my.company.com/gitblit/git/category/my.complex.repo.git",
                                "",
                                "",
                                "",
                                "1234",
                                32),
                        "http://my.company.com/gitblit/commitdiff/category!my.complex.repo.git/1234"
                },

                {
                        ContextTestUtil.getMockedContext(
                                "http://git.delwink.com/git/r/patts-qt.git",
                                "",
                                "",
                                "",
                                "607a417df243451330d91b48515b4df9eb106580",
                                32),
                        "http://git.delwink.com/git/commitdiff/patts-qt.git/607a417df243451330d91b48515b4df9eb106580"
                },
                {
                        ContextTestUtil.getMockedContext(
                                "http://my.company.com/gitblit/git/category/my.complex.repo.git",
                                "feature/feature42D",
                                "src/com/foo/FooTest.java",
                                "",
                                "1234",
                                32),
                        "http://my.company.com/gitblit/blob/category!my.complex.repo.git/1234/src!com!foo!FooTest.java#L32"
                },
                {
                        ContextTestUtil.getMockedContext(
                                "http://git.delwink.com/git/r/ljpapi.git",
                                "master",
                                "src/com/delwink/ljp/Alignment.java",
                                "",
                                "607a417df243451330d91b48515b4df9eb106580",
                                32),
                        "http://git.delwink.com/git/blob/ljpapi.git/607a417df243451330d91b48515b4df9eb106580/src!com!delwink!ljp!Alignment.java#L32"
                },
                {
                        ContextTestUtil.getMockedContext(
                                "http://git.delwink.com/git/r/ljpapi.git",
                                "master",
                                "src/com/delwink/ljp/Alignment.java",
                                "",
                                null,
                                32),
                        "http://git.delwink.com/git/blob/ljpapi.git/master/src!com!delwink!ljp!Alignment.java#L32"
                },

        };
    }

    /**
     * Acts as a data provider for contexts.
     *
     * @return Object[][]
     */
    @DataProvider
    public static Object[][] getContextsForSSL() throws MalformedURLException, RemoteNotFoundException {


        return new Object[][]{
                {
                        ContextTestUtil.getMockedContext(
                                "http://my.company.com/gitblit/git/category/my.complex.repo.git",
                                "",
                                "",
                                "",
                                "1234",
                                32),
                        "https://my.company.com/gitblit/commitdiff/category!my.complex.repo.git/1234"
                },

                {
                        ContextTestUtil.getMockedContext(
                                "http://git.delwink.com/git/r/patts-qt.git",
                                "",
                                "",
                                "",
                                "607a417df243451330d91b48515b4df9eb106580",
                                32),
                        "https://git.delwink.com/git/commitdiff/patts-qt.git/607a417df243451330d91b48515b4df9eb106580"
                },
                {
                        ContextTestUtil.getMockedContext(
                                "http://my.company.com/gitblit/git/category/my.complex.repo.git",
                                "feature/feature42D",
                                "src/com/foo/FooTest.java",
                                "",
                                "1234",
                                32),
                        "https://my.company.com/gitblit/blob/category!my.complex.repo.git/1234/src!com!foo!FooTest.java#L32"
                },
                {
                        ContextTestUtil.getMockedContext(
                                "http://git.delwink.com/git/r/ljpapi.git",
                                "master",
                                "src/com/delwink/ljp/Alignment.java",
                                "",
                                "607a417df243451330d91b48515b4df9eb106580",
                                32),
                        "https://git.delwink.com/git/blob/ljpapi.git/607a417df243451330d91b48515b4df9eb106580/src!com!delwink!ljp!Alignment.java#L32"
                },

        };
    }


    /**
     * Get the url factory.
     *
     * @return GitBlitUrlFactory
     */
    public GitBlitUrlFactory getGitBlitUrlFactory() {
        return new GitBlitUrlFactory();
    }
}
