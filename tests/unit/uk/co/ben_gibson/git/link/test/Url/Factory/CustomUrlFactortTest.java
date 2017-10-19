package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Url.Factory.CustomUrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;

import java.net.MalformedURLException;

public class CustomUrlFactortTest extends UrlFactoryTest
{
    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
         return new Object[][] {
             {
                 new CommitDescription(
                     UrlFactoryTest.mockRemote("https://example.com/foo bar/baz"),
                     UrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                 ),
                 "https://custom.host.com/custom-project/commit/bd7ab0f04151e7409d77caa296617f97352f36d3"
             },
             {
                 new CommitDescription(
                     UrlFactoryTest.mockRemote("http://custom.example.com/foo/bar"),
                     UrlFactoryTest.mockCommit("bd7ab0f04151e7409d77caa296617f97352f36d3")
                 ),
                 "https://custom.host.com/custom-project/commit/bd7ab0f04151e7409d77caa296617f97352f36d3"
             },
         };
    }

    @DataProvider
    public static Object[][] fileProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][] {
            {
                new FileDescription(
                    UrlFactoryTest.mockRemote("https://example.com/foo/bar"),
                    Branch.master(),
                    UrlFactoryTest.mockFile("src/Bar.java", "Bar.java"),
                    10
                ),
                "https://custom.host.com/custom-project/master/src/Bar.java#10"
            },
            {
                new FileDescription(
                    UrlFactoryTest.mockRemote("http://example.com/foo bar/baz"),
                    new Branch("dev-wip-[2.0.0]"),
                    UrlFactoryTest.mockFile("src/Foo Bar.java", "Foo Bar.java"),
                    null
                ),
                "https://custom.host.com/custom-project/dev-wip-[2.0.0]/src/Foo Bar.java#"
            },
        };
    }

    public UrlFactory remoteUrlFactory()
    {
        return new CustomUrlFactory(
            "https://custom.host.com/custom-project/{branch}/{filePath}/{fileName}#{line}",
            "https://custom.host.com/custom-project/commit/{commit}"
        );
    }
}
