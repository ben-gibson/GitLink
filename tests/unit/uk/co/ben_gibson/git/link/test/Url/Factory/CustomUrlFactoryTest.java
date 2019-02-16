package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProvider;
import org.junit.Test;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Factory.CustomUrlFactory;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

import java.net.MalformedURLException;

public class CustomUrlFactoryTest extends UrlFactoryTest
{
    public static CustomUrlFactory factory(
        String fileAtBranchUrlTemplate,
        String fileAtCommitUrlTemplate,
        String commitUrlTemplate
    ) {
        return new CustomUrlFactory(new URLTemplateProcessor(), fileAtBranchUrlTemplate, fileAtCommitUrlTemplate, commitUrlTemplate);
    }


    @Test(expected = UrlFactoryException.class)
    public void testRejectsInvalidCommitTemplate() throws MalformedURLException, RemoteException, UrlFactoryException
    {
        CustomUrlFactory factory = factory(
            "https://example.com",
            "https://example.com",
            "example"
        );

        factory.createUrlToCommit(
            mockRemote("https://example.com/foo bar/baz"),
            new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3")
        );
    }


    @Test(expected = UrlFactoryException.class)
    public void testRejectsInvalidFileAtCommitTemplate() throws MalformedURLException, RemoteException, UrlFactoryException
    {
        CustomUrlFactory factory = factory(
            "https://example.com",
            "example",
            "https://example.com"
        );

        factory.createUrlToFileAtCommit(
            mockRemote("https://example.com/foo bar/baz"),
            new File(".gitignore", ".gitignore"),
            new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
            null
        );
    }


    @Test(expected = UrlFactoryException.class)
    public void testRejectsInvalidFileOnBranchTemplate() throws MalformedURLException, RemoteException, UrlFactoryException
    {
        CustomUrlFactory factory = factory(
            "example",
            "https://example.com",
            "https://example.com"
        );

        factory.createUrlToFileOnBranch(
            mockRemote("https://example.com/foo bar/baz"),
            new File(".gitignore", ".gitignore"),
            Branch.master(),
            null
        );
    }


    @DataProvider
    public static Object[][] canDetermineIfFileAtCommitIsSupportedProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            {
                factory("", "", ""),
                false
            },
            {
                factory("", "https://custom.host.com/custom-project/{filePath}/{fileName}/commit/{commit}#{line}", ""),
                true
            },
        };
    }


    @DataProvider
    public static Object[][] commitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            // LEGACY
            {
                factory("", "", "https://custom.host.com/custom-project/commit/{commit}"),
                mockRemote("https://example.com/foo bar/baz"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://custom.host.com/custom-project/commit/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                factory("", "", "https://custom.host.com/custom-project/commit/{commit}"),
                mockRemote("http://custom.example.com/foo/bar"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://custom.host.com/custom-project/commit/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            // NEW
            {
                factory("", "", "https://custom.host.com/{remote:url:path}/commit/{commit}"),
                mockRemote("http://example.com/foo bar/baz"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://custom.host.com/foo%20bar/baz/commit/bd7ab0f04151e7409d77caa296617f97352f36d3"
            },
            {
                factory("", "", "{remote:url}/commit/{commit:short}"),
                mockRemote("http://custom.example.com/foo/bar"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "http://custom.example.com/foo/bar/commit/bd7ab0"
            },
            {
                factory("", "", "https://example.com/org/{remote:url:path:0}/project/{remote:url:path:1}/commit/{commit:short}"),
                mockRemote("http://custom.example.com/foo/bar"),
                new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3"),
                "https://example.com/org/foo/project/bar/commit/bd7ab0"
            },
        };
    }


    @DataProvider
    public static Object[][] fileAtCommitProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            // LEGACY
            {
                factory("", "https://custom.host.com/custom-project/{filePath}/{fileName}/commit/{commit}#{line}", ""),
                mockRemote("http://custom.example.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                new Commit("f7c244eeea9f8e4ebbeabc1500b90e656f5d0328"),
                new LineSelection(10),
                "https://custom.host.com/custom-project/src/Bar.java/commit/f7c244eeea9f8e4ebbeabc1500b90e656f5d0328#10"
            },
            // NEW
            {
                factory("", "{remote:url}/{file:path}/{file:name}/commit/{commit}#{line:start}-{line:end}", ""),
                mockRemote("http://custom.example.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                new Commit("f7c244eeea9f8e4ebbeabc1500b90e656f5d0328"),
                new LineSelection(10),
                "http://custom.example.com/foo/bar/src/Bar.java/commit/f7c244eeea9f8e4ebbeabc1500b90e656f5d0328#10-10"
            },
            {
                factory("", "{remote:url}/{file:path}/{file:name}/commit/{commit:short}#{line:start}-{line:end}", ""),
                mockRemote("http://custom.example.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                new Commit("f7c244eeea9f8e4ebbeabc1500b90e656f5d0328"),
                new LineSelection(10),
                "http://custom.example.com/foo/bar/src/Bar.java/commit/f7c244#10-10"
            },
            {
                factory("", "https://example.com/org/{remote:url:path:0}/project/{remote:url:path:1}/{remote:url:path:9}/{remote:url:path:7}/{file:path}/{file:name}/commit/{commit:short}#{line:start}-{line:end}", ""),
                mockRemote("http://custom.example.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                new Commit("f7c244eeea9f8e4ebbeabc1500b90e656f5d0328"),
                new LineSelection(10),
                "https://example.com/org/foo/project/bar/src/Bar.java/commit/f7c244#10-10"
            },
        };
    }


    @DataProvider
    public static Object[][] fileOnBranchProvider() throws MalformedURLException, RemoteException
    {
        return new Object[][]{
            // LEGACY
            {
                factory("https://custom.host.com/custom-project/{branch}/{filePath}/{fileName}#{line}", "", ""),
                mockRemote("https://example.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                Branch.master(),
                new LineSelection(10),
                "https://custom.host.com/custom-project/master/src/Bar.java#10"
            },
            {
                factory("https://custom.host.com/custom-project/{branch}/{filePath}/{fileName}#{line}", "", ""),
                mockRemote("http://example.com/foo bar/baz"),
                new File("src/Foo Bar.java", "Foo Bar.java"),
                new Branch("dev-wip-[2.0.0]"),
                null,
                "https://custom.host.com/custom-project/dev-wip-%5B2.0.0%5D/src/Foo%20Bar.java#0"
            },
            {
                factory("https://custom.host.com/custom-project/{branch}/{filePath}/{fileName}#{line}", "", ""),
                mockRemote("https://example.com/foo/bar"),
                new File(".gitignore", ".gitignore"),
                Branch.master(),
                new LineSelection(10),
                "https://custom.host.com/custom-project/master/.gitignore#10"
            },
            {
                factory("https://custom.host.com/custom-project/{branch}/{filePath}/{fileName}?line={line}", "", ""),
                mockRemote("https://example.com/foo/bar"),
                new File(".gitignore", ".gitignore"),
                Branch.master(),
                new LineSelection(10),
                "https://custom.host.com/custom-project/master/.gitignore?line=10"
            },
            // NEW
            {
                factory("{remote:url}/{branch}/{file:path}/{file:name}#{line:start}-{line:end}", "", ""),
                mockRemote("https://example.com/foo/bar"),
                new File("src/Bar.java", "Bar.java"),
                Branch.master(),
                new LineSelection(10),
                "https://example.com/foo/bar/master/src/Bar.java#10-10"
            },
            {
                factory("http://my.host.com/{remote:url:path}/{branch}/{file:path}/{file:name}", "", ""),
                mockRemote("http://example.com/foo bar/baz"),
                new File("src/Foo Bar.java", "Foo Bar.java"),
                new Branch("dev-wip-[2.0.0]"),
                null,
                "http://my.host.com/foo%20bar/baz/dev-wip-%5B2.0.0%5D/src/Foo%20Bar.java"
            },
            {
                factory("https://custom.host.com/custom-project/{branch}/{file:path}/{file:name}?line={line:start}", "", ""),
                mockRemote("https://example.com/foo/bar"),
                new File(".gitignore", ".gitignore"),
                Branch.master(),
                new LineSelection(10),
                "https://custom.host.com/custom-project/master/.gitignore?line=10"
            },
        };
    }
}
