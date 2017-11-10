package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.GitBlitUrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;

import java.net.MalformedURLException;

public class GitBlitUrlFactoryTest extends UrlFactoryTest
{

    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
                {
                        new CommitDescription(
                                mockRemote("http://my.company.com/gitblit/git/category/my.complex.repo"),
                                UrlFactoryTest.mockCommit("1234")
                        ),
                        "http://my.company.com/gitblit/commitdiff/category!my.complex.repo.git/1234"
                },
                {
                        new CommitDescription(
                                mockRemote("http://my.company.com/git/r/patts-qt"),
                                UrlFactoryTest.mockCommit("1234")
                        ),
                        "http://my.company.com/git/commitdiff/patts-qt.git/1234"
                },
                {
                        new CommitDescription(
                                mockRemote("http://my.company.com/r/patts-qt"),
                                UrlFactoryTest.mockCommit("1234")
                        ),
                        "http://my.company.com/commitdiff/patts-qt.git/1234"
                },
                {
                        new CommitDescription(
                                mockRemote("http://my.company.com/git/patts-qt"),
                                UrlFactoryTest.mockCommit("1234")
                        ),
                        "http://my.company.com/commitdiff/patts-qt.git/1234"
                },

        };
    }

    @DataProvider
    public static Object[][] fileProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{

                {
                        new FileDescription(
                                mockRemote("http://my.company.com/gitblit/git/category/my.complex.repo"),
                                new Branch("feature/feature42D"),
                                mockFile("src/com/foo/FooTest.java", "FooTest.java"),
                                32
                        ),
                        "http://my.company.com/gitblit/blob/category!my.complex.repo.git/feature!feature42D/src!com!foo!FooTest.java#L32",
                },
                {
                        new FileDescription(
                                mockRemote("http://my.company.com/git/r/ljpapi"),
                                new Branch("feature/feature42D"),
                                mockFile("src/com/company/ljp/Alignment.java", "Alignment.java"),
                                32
                        ),
                        "http://my.company.com/git/blob/ljpapi.git/feature!feature42D/src!com!company!ljp!Alignment.java#L32"

                },
                {
                        new FileDescription(
                                mockRemote("http://my.company.com/r/ljpapi"),
                                new Branch("feature/feature42D"),
                                mockFile("src/com/company/ljp/Alignment.java", "Alignment.java"),
                                32
                        ),
                        "http://my.company.com/blob/ljpapi.git/feature!feature42D/src!com!company!ljp!Alignment.java#L32"

                },
                {
                        new FileDescription(
                                mockRemote("http://my.company.com/git/ljpapi"),
                                new Branch("feature/feature42D"),
                                mockFile("src/com/company/ljp/Alignment.java", "Alignment.java"),
                                32
                        ),
                        "http://my.company.com/blob/ljpapi.git/feature!feature42D/src!com!company!ljp!Alignment.java#L32"

                },

        };
    }

    public UrlFactory remoteUrlFactory()
    {
        return new GitBlitUrlFactory();
    }
}