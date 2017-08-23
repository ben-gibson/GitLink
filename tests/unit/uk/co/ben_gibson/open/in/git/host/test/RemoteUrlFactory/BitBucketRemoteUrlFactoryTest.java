package uk.co.ben_gibson.open.in.git.host.test.RemoteUrlFactory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import uk.co.ben_gibson.open.in.git.host.Git.Branch;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.BitBucketRemoteUrlFactory;

public class BitBucketRemoteUrlFactoryTest extends RemoteUrlFactoryTest
{
    @DataProvider
    public static Object[][] commitProvider()
    {
        return new Object[][] {
            {
                "https://example@bitbucket.org/foo bar/bar",
                RemoteUrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://bitbucket.org/foo%20bar/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3",
                false
            },
            {
                "https://bitbucket.org/foo bar/bar",
                RemoteUrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://bitbucket.org/foo%20bar/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3",
                false
            },
            {
                "http://foo-bar.com/foo/bar",
                RemoteUrlFactoryTest.mockCommit("0df948a98048a3b30911d974414dfe0ef22a1724"),
                "http://foo-bar.com/foo/bar/commits/0df948a98048a3b30911d974414dfe0ef22a1724",
                false
            },
            {
                "http://bitbucket.com/foo/bar",
                RemoteUrlFactoryTest.mockCommit("0df948a98048a3b30911d974414dfe0ef22a1724"),
                "https://bitbucket.com/foo/bar/commits/0df948a98048a3b30911d974414dfe0ef22a1724",
                true
            },
        };
    }

    @DataProvider
    public static Object[][] fileProvider()
    {
        return new Object[][] {
            {
                "https://bitbucket.org/foo/bar/",
                Branch.master(),
                10,
                RemoteUrlFactoryTest.mockFile("src/Bar.java", "Bar.java"),
                "https://bitbucket.org/foo/bar/src/HEAD/src/Bar.java?at=master#Bar.java-10",
                false
            },
            {
                "http://example@bitbucket.org/foo/bar/",
                new Branch("dev"),
                null,
                RemoteUrlFactoryTest.mockFile("src/Bar.java", "Bar.java"),
                "http://bitbucket.org/foo/bar/src/HEAD/src/Bar.java?at=dev",
                false
            },
            {
                "http://foo-bar.org/foo bar/bar",
                new Branch("dev-wip[2.0.0]"),
                null,
                RemoteUrlFactoryTest.mockFile("/src/foo/Foo Bar Baz.java", "Foo Bar Baz.java"),
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
