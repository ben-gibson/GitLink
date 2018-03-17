package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.Url.Factory.CustomUrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;

import java.net.MalformedURLException;

public class CustomUrlFactoryTest extends UrlFactoryTest
{
    @Test(expected = UrlFactoryException.class)
    public void testRejectsInvalidCommitTemplate() throws MalformedURLException, RemoteException, UrlFactoryException
    {
        CustomUrlFactory factory = new CustomUrlFactory("https://example.com", "https://example.com", "example");

        factory.createUrlToCommit(
            UrlFactoryTest.mockRemote("https://example.com/foo bar/baz"),
            new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3")
        );
    }


    @Test(expected = UrlFactoryException.class)
    public void testRejectsInvalidFileAtCommitTemplate() throws MalformedURLException, RemoteException, UrlFactoryException
    {
        CustomUrlFactory factory = new CustomUrlFactory("https://example.com", "example", "https://example.com");

        factory.createUrlToFileAtCommit(
            UrlFactoryTest.mockRemote("https://example.com/foo bar/baz"),
            new File(".gitignore", ".gitignore"),
            new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
            null
        );
    }


    @Test(expected = UrlFactoryException.class)
    public void testRejectsInvalidFileOnBranchTemplate() throws MalformedURLException, RemoteException, UrlFactoryException
    {
        CustomUrlFactory factory = new CustomUrlFactory("example", "https://example.com", "https://example.com");

        factory.createUrlToFileOnBranch(
            UrlFactoryTest.mockRemote("https://example.com/foo bar/baz"),
            new File(".gitignore", ".gitignore"),
            Branch.master(),
            null
        );
    }


    @Test
    @UseDataProvider("canDetermineIfFileAtCommitIsSupportedProvider")
    public void testCanDetermineIfFileAtCommitIsSupported(CustomUrlFactory factory, boolean expected)
    {
        assertSame(expected, factory.canOpenFileAtCommit());
    }


    @DataProvider
    public static Object[][] canDetermineIfFileAtCommitIsSupportedProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new CustomUrlFactory("", "", ""),
                false
            },
            {
                new CustomUrlFactory("", "https://custom.host.com/custom-project/{filePath}/{fileName}/commit/{commit}#{line}", ""),
                true
            },
        };
    }


    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new CustomUrlFactory("", "", "https://custom.host.com/custom-project/commit/{commit}"),
                UrlFactoryTest.mockRemote("https://example.com/foo bar/baz"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://custom.host.com/custom-project/commit/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                new CustomUrlFactory("", "", "https://custom.host.com/custom-project/commit/{commit}"),
                UrlFactoryTest.mockRemote("http://custom.example.com/foo/bar"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://custom.host.com/custom-project/commit/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
        };
    }


    @DataProvider
    public static Object[][] fileAtCommitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new CustomUrlFactory("", "https://custom.host.com/custom-project/{filePath}/{fileName}/commit/{commit}#{line}", ""),
                mockRemote("http://custom.example.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                new Commit("f7c244eeea9f8e4ebbeabc1500b90e656f5d0328"),
                10,
                "https://custom.host.com/custom-project/src/Bar.java/commit/f7c244eeea9f8e4ebbeabc1500b90e656f5d0328#10"
            },
        };
    }


    @DataProvider
    public static Object[][] fileOnBranchProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                new CustomUrlFactory("https://custom.host.com/custom-project/{branch}/{filePath}/{fileName}#{line}", "", ""),
                UrlFactoryTest.mockRemote("https://example.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                Branch.master(),
                10,
                "https://custom.host.com/custom-project/master/src/Bar.java#10"
            },
            {
                new CustomUrlFactory("https://custom.host.com/custom-project/{branch}/{filePath}/{fileName}#{line}", "", ""),
                UrlFactoryTest.mockRemote("http://example.com/foo bar/baz"),
                new File("src/Foo Bar.java", "Foo Bar.java"),
                new Branch("dev-wip-[2.0.0]"),
                null,
                "https://custom.host.com/custom-project/dev-wip-[2.0.0]/src/Foo Bar.java"
            },
            {
                new CustomUrlFactory("https://custom.host.com/custom-project/{branch}/{filePath}/{fileName}#{line}", "", ""),
                UrlFactoryTest.mockRemote("https://example.com/foo/bar"),
                new File(".gitignore", ".gitignore"),
                Branch.master(),
                10,
                "https://custom.host.com/custom-project/master/.gitignore#10"
            },
            {
                new CustomUrlFactory("https://custom.host.com/custom-project/{branch}/{filePath}/{fileName}?line={line}", "", ""),
                UrlFactoryTest.mockRemote("https://example.com/foo/bar"),
                new File(".gitignore", ".gitignore"),
                Branch.master(),
                10,
                "https://custom.host.com/custom-project/master/.gitignore?line=10"
            },
        };
    }
}
