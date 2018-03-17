package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import org.junit.Test;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.Url.Factory.BitBucketUrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;
import java.net.MalformedURLException;

public class BitBucketUrlFactoryTest extends UrlFactoryTest
{
    @Test
    public void testCanDetermineIfFileAtCommitIsSupported()
    {
        assertTrue(this.remoteUrlFactory().canOpenFileAtCommit());
    }


    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new BitBucketUrlFactory(),
                mockRemote("https://example@bitbucket.org/foo bar/bar"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://bitbucket.org/foo%20bar/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new BitBucketUrlFactory(),
                mockRemote("https://bitbucket.org/foo bar/bar"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://bitbucket.org/foo%20bar/bar/commits/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new BitBucketUrlFactory(),
                mockRemote("http://foo-bar.com/foo/bar"),
                new Commit("0df948a98048a3b30911d974414dfe0ef22a1724"),
                "http://foo-bar.com/foo/bar/commits/0df948a98048a3b30911d974414dfe0ef22a1724"
            },
        };
    }


    @DataProvider
    public static Object[][] fileAtCommitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new BitBucketUrlFactory(),
                mockRemote("https://bitbucket.org/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                new Commit("f7c244eeea9f8e4ebbeabc1500b90e656f5d0328"),
                10,
                "https://bitbucket.org/foo/bar/src/f7c244eeea9f8e4ebbeabc1500b90e656f5d0328/src/Bar.java#Bar.java-10"
            },
        };
    }


    @DataProvider
    public static Object[][] fileOnBranchProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new BitBucketUrlFactory(),
                mockRemote("https://bitbucket.org/foo/bar/"),
                new File("src/Bar.java", "Bar.java"),
                Branch.master(),
                10,
                "https://bitbucket.org/foo/bar/src/HEAD/src/Bar.java?at=master#Bar.java-10"
            },
            {
                new BitBucketUrlFactory(),
                mockRemote("http://example@bitbucket.org/foo/bar/"),
                new File("src/Bar.java", "Bar.java"),
                new Branch("dev"),
                null,
                "http://bitbucket.org/foo/bar/src/HEAD/src/Bar.java?at=dev"
            },
            {
                new BitBucketUrlFactory(),
                mockRemote("https://foo-bar.org/foo bar/bar"),
                new File("/src/foo/Foo Bar Baz.java", "Foo Bar Baz.java"),
                new Branch("dev-wip[2.0.0]"),
                null,
                "https://foo-bar.org/foo%20bar/bar/src/HEAD/src/foo/Foo%20Bar%20Baz.java?at=dev-wip[2.0.0]"
            },
        };
    }

    public UrlFactory remoteUrlFactory()
    {
        return new BitBucketUrlFactory();
    }
}
