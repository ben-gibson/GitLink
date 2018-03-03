package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import org.junit.Test;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.Url.Factory.GitHubUrlFactory;

import java.net.MalformedURLException;

public class GitHubUrlFactoryTest extends UrlFactoryTest
{
    @Test
    public void testCanDetermineIfFileAtCommitIsSupported()
    {
        assertTrue(new GitHubUrlFactory().canOpenFileAtCommit());
    }


    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new GitHubUrlFactory(),
                mockRemote("https://github.com/foo/bar"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://github.com/foo/bar/commit/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new GitHubUrlFactory(),
                mockRemote("https://github.com/foo bar/baz"),
                new Commit("40ec791cdd904557793e200c93f3118043ec18af"),
                "https://github.com/foo%20bar/baz/commit/40ec791cdd904557793e200c93f3118043ec18af"
            },
            {
                new GitHubUrlFactory(),
                mockRemote("http://github.com/foo/bar"),
                new Commit("0df948a98048a3b30911d974414dfe0ef22a1724"),
                "http://github.com/foo/bar/commit/0df948a98048a3b30911d974414dfe0ef22a1724"
            },
        };
    }


    @DataProvider
    public static Object[][] fileAtCommitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new GitHubUrlFactory(),
                mockRemote("http://github.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                new Commit("f7c244eeea9f8e4ebbeabc1500b90e656f5d0328"),
                10,
                "http://github.com/foo/bar/blob/f7c244eeea9f8e4ebbeabc1500b90e656f5d0328/src/Bar.java#L10"
            },
        };
    }


    @DataProvider
    public static Object[][] fileOnBranchProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new GitHubUrlFactory(),
                mockRemote("https://github.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                Branch.master(),
                10,
                "https://github.com/foo/bar/blob/master/src/Bar.java#L10"
            },
            {
                new GitHubUrlFactory(),
                mockRemote("https://github.com/foo/bar"),
                new File("/src/Bar Bar/Baz.java", "Baz.java"),
                new Branch("feature-foo-[PRO-123]"),
                null,
                "https://github.com/foo/bar/blob/feature-foo-%5BPRO-123%5D/src/Bar%20Bar/Baz.java"
            },
            {
                new GitHubUrlFactory(),
                mockRemote("http://github.com/foo/bar"),
                new File("/src/Bar Bar/Baz.java", "Baz.java"),
                new Branch("dev"),
                null,
                "http://github.com/foo/bar/blob/dev/src/Bar%20Bar/Baz.java"
            },
            {
                new GitHubUrlFactory(),
                mockRemote("https://github.com/foo bar/baz"),
                new File("需求0920-2017/0928.sql", "0928.sql"),
                Branch.master(),
                15,
                "https://github.com/foo%20bar/baz/blob/master/需求0920-2017/0928.sql#L15"
            },
        };
    }
}
