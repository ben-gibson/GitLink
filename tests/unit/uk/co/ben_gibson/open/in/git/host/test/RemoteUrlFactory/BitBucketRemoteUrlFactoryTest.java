package uk.co.ben_gibson.open.in.git.host.test.RemoteUrlFactory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import uk.co.ben_gibson.open.in.git.host.Git.Branch;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteCommitDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description.RemoteFileDescription;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.BitBucketRemoteUrlFactory;

import java.net.MalformedURLException;

public class BitBucketRemoteUrlFactoryTest extends RemoteUrlFactoryTest
{
    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new RemoteCommitDescription(
                    RemoteUrlFactoryTest.mockRemote("https://example@bitbucket.org/foo bar/bar"),
                    RemoteUrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "https://bitbucket.org/foo%20bar/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3",
                false
            },
            {
                new RemoteCommitDescription(
                    RemoteUrlFactoryTest.mockRemote("https://bitbucket.org/foo bar/bar"),
                    RemoteUrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "https://bitbucket.org/foo%20bar/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3",
                false
            },
            {
                new RemoteCommitDescription(
                    RemoteUrlFactoryTest.mockRemote("http://foo-bar.com/foo/bar"),
                    RemoteUrlFactoryTest.mockCommit("0df948a98048a3b30911d974414dfe0ef22a1724")
                ),
                "http://foo-bar.com/foo/bar/commits/0df948a98048a3b30911d974414dfe0ef22a1724",
                false
            },
            {
                new RemoteCommitDescription(
                    RemoteUrlFactoryTest.mockRemote("http://bitbucket.com/foo/bar"),
                    RemoteUrlFactoryTest.mockCommit("0df948a98048a3b30911d974414dfe0ef22a1724")
                ),
                "https://bitbucket.com/foo/bar/commits/0df948a98048a3b30911d974414dfe0ef22a1724",
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
                    RemoteUrlFactoryTest.mockRemote("https://bitbucket.org/foo/bar/"),
                    Branch.master(),
                    RemoteUrlFactoryTest.mockFile("src/Bar.java", "Bar.java"),
                    10
                ),
                "https://bitbucket.org/foo/bar/src/HEAD/src/Bar.java?at=master#Bar.java-10",
                false
            },
            {
                new RemoteFileDescription(
                    RemoteUrlFactoryTest.mockRemote("http://example@bitbucket.org/foo/bar/"),
                    new Branch("dev"),
                    RemoteUrlFactoryTest.mockFile("src/Bar.java", "Bar.java"),
                    null
                ),
                "http://bitbucket.org/foo/bar/src/HEAD/src/Bar.java?at=dev",
                false
            },
            {
                new RemoteFileDescription(
                    RemoteUrlFactoryTest.mockRemote("http://foo-bar.org/foo bar/bar"),
                    new Branch("dev-wip[2.0.0]"),
                    RemoteUrlFactoryTest.mockFile("/src/foo/Foo Bar Baz.java", "Foo Bar Baz.java"),
                    null
                ),
                "https://foo-bar.org/foo%20bar/bar/src/HEAD/src/foo/Foo%20Bar%20Baz.java?at=dev-wip[2.0.0]",
                true
            },
        };
    }

    public RemoteUrlFactory remoteUrlFactory()
    {
        return new BitBucketRemoteUrlFactory();
    }
}
