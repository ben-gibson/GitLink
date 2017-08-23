package uk.co.ben_gibson.open.in.git.host.test.RemoteUrlFactory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import uk.co.ben_gibson.open.in.git.host.Git.Branch;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.GitHubRemoteUrlFactory;

public class GitHubRemoteUrlFactoryTest extends RemoteUrlFactoryTest
{
    @DataProvider
    public static Object[][] commitProvider()
    {
        return new Object[][] {
            {
                "https://github.com/foo/bar",
                RemoteUrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://github.com/foo/bar/commit/bd7ab0f04151e7409d77caa296617f97352f36d3",
                false
            },
            {
                "https://github.com/foo bar/baz",
                RemoteUrlFactoryTest.mockCommit("40ec791cdd904557793e200c93f3118043ec18af"),
                "https://github.com/foo%20bar/baz/commit/40ec791cdd904557793e200c93f3118043ec18af",
                false
            },
            {
                "http://github.com/foo/bar",
                RemoteUrlFactoryTest.mockCommit("0df948a98048a3b30911d974414dfe0ef22a1724"),
                "http://github.com/foo/bar/commit/0df948a98048a3b30911d974414dfe0ef22a1724",
                false
            },
            {
                "http://github.com/foo/bar",
                RemoteUrlFactoryTest.mockCommit("0df948a98048a3b30911d974414dfe0ef22a1724"),
                "https://github.com/foo/bar/commit/0df948a98048a3b30911d974414dfe0ef22a1724",
                true
            },
        };
    }

    @DataProvider
    public static Object[][] fileProvider()
    {
        return new Object[][] {
            {
                "https://github.com/foo/bar",
                Branch.master(),
                10,
                RemoteUrlFactoryTest.mockFile("src/Bar.java", "Bar.java"),
                "https://github.com/foo/bar/blob/master/src/Bar.java#L10",
                false
            },
            {
                "https://github.com/foo/bar",
                new Branch("feature-foo-[PRO-123]"),
                null,
                RemoteUrlFactoryTest.mockFile("/src/Bar Bar/Baz.java", "Baz.java"),
                "https://github.com/foo/bar/blob/feature-foo-%5BPRO-123%5D/src/Bar%20Bar/Baz.java",
                false
            },
            {
                "http://github.com/foo/bar",
                new Branch("dev"),
                null,
                RemoteUrlFactoryTest.mockFile("/src/Bar Bar/Baz.java", "Baz.java"),
                "http://github.com/foo/bar/blob/dev/src/Bar%20Bar/Baz.java",
                false
            },
            {
                "https://github.com/foo bar/baz",
                Branch.master(),
                null,
                RemoteUrlFactoryTest.mockFile("resources/Bar Baz.java", "Bar Baz.java"),
                "https://github.com/foo%20bar/baz/blob/master/resources/Bar%20Baz.java",
                true
            },
        };
    }

    public RemoteUrlFactory remoteUrlFactory()
    {
        return new GitHubRemoteUrlFactory();
    }
}
