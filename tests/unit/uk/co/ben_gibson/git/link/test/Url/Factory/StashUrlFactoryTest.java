package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.StashUrlFactory;
import java.net.MalformedURLException;

public class StashUrlFactoryTest extends UrlFactoryTest
{
    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new CommitDescription(
                    UrlFactoryTest.mockRemote("https://stash.example.com/foo bar/baz"),
                    UrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "https://stash.example.com/projects/foo%20bar/repos/baz/commits/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new CommitDescription(
                    UrlFactoryTest.mockRemote("http://stash.example.com/foo/bar"),
                    UrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "http://stash.example.com/projects/foo/repos/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
        };
    }

    @DataProvider
    public static Object[][] fileProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new FileDescription(
                    UrlFactoryTest.mockRemote("https://stash.example.com/foo/bar"),
                    Branch.master(),
                    UrlFactoryTest.mockFile("src/Bar.java", "Bar.java"),
                    10
                ),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Bar.java?at=refs/heads/master#10",
            },
            {
                new FileDescription(
                    UrlFactoryTest.mockRemote("http://stash.example.com/foo bar/baz"),
                    new Branch("dev-wip-[2.0.0]"),
                    UrlFactoryTest.mockFile("src/Foo Bar.java", "Foo Bar.java"),
                    null
                ),
                "http://stash.example.com/projects/foo%20bar/repos/baz/browse/src/Foo%20Bar.java?at=refs/heads/dev-wip-[2.0.0]"
            },
        };
    }

    public UrlFactory remoteUrlFactory()
    {
        return new StashUrlFactory();
    }
}
