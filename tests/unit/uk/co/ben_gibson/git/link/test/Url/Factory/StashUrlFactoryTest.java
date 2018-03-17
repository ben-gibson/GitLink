package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import org.junit.Test;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.Url.Factory.GitHubUrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.StashUrlFactory;

import java.net.MalformedURLException;

public class StashUrlFactoryTest extends UrlFactoryTest
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
                new StashUrlFactory(),
                UrlFactoryTest.mockRemote("https://stash.example.com/foo bar/baz"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://stash.example.com/projects/foo%20bar/repos/baz/commits/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new StashUrlFactory(),
                UrlFactoryTest.mockRemote("http://stash.example.com/foo/bar"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "http://stash.example.com/projects/foo/repos/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
        };
    }


    @DataProvider
    public static Object[][] fileAtCommitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new StashUrlFactory(),
                mockRemote("https://stash.example.com/foo bar/baz"),
                new File("src/Bar.java", "Bar.java"),
                new Commit("f7c244eeea9f8e4ebbeabc1500b90e656f5d0328"),
                10,
                "https://stash.example.com/projects/foo%20bar/repos/baz/browse/src/Bar.java?at=f7c244eeea9f8e4ebbeabc1500b90e656f5d0328#10"
            },
        };
    }


    @DataProvider
    public static Object[][] fileOnBranchProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new StashUrlFactory(),
                UrlFactoryTest.mockRemote("https://stash.example.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                Branch.master(),
                10,
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Bar.java?at=refs/heads/master#10",
            },
            {
                new StashUrlFactory(),
                UrlFactoryTest.mockRemote("http://stash.example.com/foo bar/baz"),
                new File("src/Foo Bar.java", "Foo Bar.java"),
                new Branch("dev-wip-[2.0.0]"),
                null,
                "http://stash.example.com/projects/foo%20bar/repos/baz/browse/src/Foo%20Bar.java?at=refs/heads/dev-wip-[2.0.0]"
            },
        };
    }
}
