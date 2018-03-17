package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import org.junit.Test;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import uk.co.ben_gibson.git.link.Url.Factory.GitBlitUrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;

import java.net.MalformedURLException;

public class GitBlitUrlFactoryTest extends UrlFactoryTest
{
    @Test
    public void testCanDetermineIfFileAtCommitIsSupported()
    {
        assertFalse(new GitBlitUrlFactory().canOpenFileAtCommit());
    }


    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new GitBlitUrlFactory(),
                mockRemote("http://my.company.com/gitblit/git/category/my.complex.repo"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "http://my.company.com/gitblit/commitdiff/category!my.complex.repo.git/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new GitBlitUrlFactory(),
                mockRemote("http://my.company.com/git/r/patts-qt"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "http://my.company.com/git/commitdiff/patts-qt.git/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new GitBlitUrlFactory(),
                mockRemote("http://my.company.com/r/patts-qt"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "http://my.company.com/commitdiff/patts-qt.git/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new GitBlitUrlFactory(),
                mockRemote("http://my.company.com/git/patts-qt"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "http://my.company.com/commitdiff/patts-qt.git/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },

        };
    }


    @DataProvider
    public static Object[][] fileAtCommitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new GitBlitUrlFactory(),
                mockRemote("http://my.company.com/gitblit/git/category/my.complex.repo"),
                new File("src/com/foo/FooTest.java", "FooTest.java"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                32,
                "http://my.company.com/gitblit/blob/category!my.complex.repo.git/feature!feature42D/src!com!foo!FooTest.java#L32",
            },
        };
    }


    @DataProvider
    public static Object[][] fileOnBranchProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new GitBlitUrlFactory(),
                mockRemote("http://my.company.com/gitblit/git/category/my.complex.repo"),
                new File("src/com/foo/FooTest.java", "FooTest.java"),
                new Branch("feature/feature42D"),
                32,
                "http://my.company.com/gitblit/blob/category!my.complex.repo.git/feature!feature42D/src!com!foo!FooTest.java#L32",
            },
            {
                new GitBlitUrlFactory(),
                mockRemote("http://my.company.com/git/r/ljpapi"),
                new File("src/com/company/ljp/Alignment.java", "Alignment.java"),
                new Branch("feature/feature42D"),
                32,
                "http://my.company.com/git/blob/ljpapi.git/feature!feature42D/src!com!company!ljp!Alignment.java#L32"

            },
            {
                new GitBlitUrlFactory(),
                mockRemote("http://my.company.com/r/ljpapi"),
                new File("src/com/company/ljp/Alignment.java", "Alignment.java"),
                new Branch("feature/feature42D"),
                32,
                "http://my.company.com/blob/ljpapi.git/feature!feature42D/src!com!company!ljp!Alignment.java#L32"

            },
            {
                new GitBlitUrlFactory(),
                mockRemote("http://my.company.com/git/ljpapi"),
                new File("src/com/company/ljp/Alignment.java", "Alignment.java"),
                new Branch("feature/feature42D"),
                32,
                "http://my.company.com/blob/ljpapi.git/feature!feature42D/src!com!company!ljp!Alignment.java#L32"
            },
        };
    }


    @Override
    public void testCanCreateUrlToFileAtCommit(UrlFactory urlFactory, Remote remote, File file, Commit commit, Integer lineNumber, String expected) throws UrlFactoryException, RemoteException
    {
        // tmp until support can be added
    }
}