package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Url.Factory.BitBucketUrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;
import java.net.MalformedURLException;

public class BitBucketUrlFactoryTest extends UrlFactoryTest
{
    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new CommitDescription(
                    mockRemote("https://example@bitbucket.org/foo bar/bar"),
                    mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "https://bitbucket.org/foo%20bar/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new CommitDescription(
                    mockRemote("https://bitbucket.org/foo bar/bar"),
                    mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "https://bitbucket.org/foo%20bar/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new CommitDescription(
                    mockRemote("http://foo-bar.com/foo/bar"),
                    mockCommit("0df948a98048a3b30911d974414dfe0ef22a1724")
                ),
                "http://foo-bar.com/foo/bar/commits/0df948a98048a3b30911d974414dfe0ef22a1724"
            },
        };
    }

    @DataProvider
    public static Object[][] fileProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new FileDescription(
                    mockRemote("https://bitbucket.org/foo/bar/"),
                    Branch.master(),
                    mockFile("src/Bar.java", "Bar.java"),
                    10
                ),
                "https://bitbucket.org/foo/bar/src/HEAD/src/Bar.java?at=master#Bar.java-10"
            },
            {
                new FileDescription(
                    mockRemote("http://example@bitbucket.org/foo/bar/"),
                    new Branch("dev"),
                    mockFile("src/Bar.java", "Bar.java"),
                    null
                ),
                "http://bitbucket.org/foo/bar/src/HEAD/src/Bar.java?at=dev"
            },
            {
                new FileDescription(
                    mockRemote("https://foo-bar.org/foo bar/bar"),
                    new Branch("dev-wip[2.0.0]"),
                    mockFile("/src/foo/Foo Bar Baz.java", "Foo Bar Baz.java"),
                    null
                ),
                "https://foo-bar.org/foo%20bar/bar/src/HEAD/src/foo/Foo%20Bar%20Baz.java?at=dev-wip[2.0.0]"
            },
        };
    }

    public UrlFactory remoteUrlFactory()
    {
        return new BitBucketUrlFactory();
    }
}
