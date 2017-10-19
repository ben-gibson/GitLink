package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.GitHubUrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;

import java.net.MalformedURLException;

public class GitHubUrlFactoryTest extends UrlFactoryTest
{
    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new CommitDescription(
                    mockRemote("https://github.com/foo/bar"),
                    mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                ),
                "https://github.com/foo/bar/commit/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new CommitDescription(
                    mockRemote("https://github.com/foo bar/baz"),
                    mockCommit("40ec791cdd904557793e200c93f3118043ec18af")
                ),
                "https://github.com/foo%20bar/baz/commit/40ec791cdd904557793e200c93f3118043ec18af"
            },
            {
                new CommitDescription(
                    mockRemote("http://github.com/foo/bar"),
                    mockCommit("0df948a98048a3b30911d974414dfe0ef22a1724")
                ),
                "http://github.com/foo/bar/commit/0df948a98048a3b30911d974414dfe0ef22a1724"
            },
        };
    }

    @DataProvider
    public static Object[][] fileProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new FileDescription(
                    mockRemote("https://github.com/foo/bar"),
                    Branch.master(),
                    mockFile("src/Bar.java", "Bar.java"),
                    10
                ),
                "https://github.com/foo/bar/blob/master/src/Bar.java#L10"
            },
            {
                new FileDescription(
                    mockRemote("https://github.com/foo/bar"),
                    new Branch("feature-foo-[PRO-123]"),
                    mockFile("/src/Bar Bar/Baz.java", "Baz.java"),
                    null
                ),
                "https://github.com/foo/bar/blob/feature-foo-%5BPRO-123%5D/src/Bar%20Bar/Baz.java"
            },
            {
                new FileDescription(
                    mockRemote("http://github.com/foo/bar"),
                    new Branch("dev"),
                    mockFile("/src/Bar Bar/Baz.java", "Baz.java"),
                    null
                ),
                "http://github.com/foo/bar/blob/dev/src/Bar%20Bar/Baz.java"
            },
            {
                new FileDescription(
                    mockRemote("https://github.com/foo bar/baz"),
                    Branch.master(),
                    mockFile("需求0920-2017/0928.sql", "0928.sql"),
                    15
                ),
                "https://github.com/foo%20bar/baz/blob/master/需求0920-2017/0928.sql#L15"
            },
        };
    }

    public UrlFactory remoteUrlFactory()
    {
        return new GitHubUrlFactory();
    }
}
