package uk.co.ben_gibson.open.in.git.host.test.RemoteUrlFactory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import uk.co.ben_gibson.open.in.git.host.Git.Branch;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteCommitDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteFileDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.StashRemoteUrlFactory;

import java.net.MalformedURLException;

public class StashRemoteUrlFactoryTest extends RemoteUrlFactoryTest
{
    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new RemoteCommitDescription(
                    RemoteUrlFactoryTest.mockRemote("https://stash.example.com/foo bar/baz"),
                    RemoteUrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "https://stash.example.com/projects/foo%20bar/repos/baz/commits/bd7ab0f04151e7409d77caa296617f97352f36d3",
                false
            },
            {
                new RemoteCommitDescription(
                    RemoteUrlFactoryTest.mockRemote("http://stash.example.com/foo/bar"),
                    RemoteUrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "http://stash.example.com/projects/foo/repos/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3",
                false
            },
            {
                new RemoteCommitDescription(
                    RemoteUrlFactoryTest.mockRemote("http://stash.example.com/foo/bar"),
                    RemoteUrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "https://stash.example.com/projects/foo/repos/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3",
                true
            },
        };
    }

    @DataProvider
    public static Object[][] fileProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new RemoteFileDescription(
                    RemoteUrlFactoryTest.mockRemote("https://stash.example.com/foo/bar"),
                    Branch.master(),
                    RemoteUrlFactoryTest.mockFile("src/Bar.java", "Bar.java"),
                    10
                ),
                "https://stash.example.com/projects/foo/repos/bar/browse/src/Bar.java?at=refs/heads/master#10",
                false
            },
            {
                new RemoteFileDescription(
                    RemoteUrlFactoryTest.mockRemote("http://stash.example.com/foo bar/baz"),
                    new Branch("dev-wip-[2.0.0]"),
                    RemoteUrlFactoryTest.mockFile("src/Foo Bar.java", "Foo Bar.java"),
                    null
                ),
                "http://stash.example.com/projects/foo%20bar/repos/baz/browse/src/Foo%20Bar.java?at=refs/heads/dev-wip-[2.0.0]",
                false
            },
            {
                new RemoteFileDescription(
                    RemoteUrlFactoryTest.mockRemote("http://stash.example.com/foo/bar"),
                    new Branch("dev"),
                    RemoteUrlFactoryTest.mockFile("Foo.java", "Foo.java"),
                    null
                ),
                "https://stash.example.com/projects/foo/repos/bar/browse/Foo.java?at=refs/heads/dev",
                true
            },
        };
    }

    public RemoteUrlFactory remoteUrlFactory()
    {
        return new StashRemoteUrlFactory();
    }
}
