package uk.co.ben_gibson.git.link.test.Git;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import git4idea.commands.Git;
import git4idea.repo.GitRemote;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.Remote;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class RemoteTest extends TestCase
{
    public void testReturnsExpectedName()
    {
        String name = "origin";

        GitRemote gitRemote = mock(GitRemote.class);
        Git git = mock(Git.class);

        when(gitRemote.getName()).thenReturn(name);

        Remote remote = new Remote(git, gitRemote);

        assertEquals(name, remote.name());
    }

    @Test
    @UseDataProvider("urlProvider")
    public void testReturnsExpectedUrl(String url, String expected) throws RemoteException
    {
        GitRemote gitRemote = mock(GitRemote.class);
        Git git = mock(Git.class);

        when(gitRemote.getFirstUrl()).thenReturn(url);

        Remote remote = new Remote(git, gitRemote);

        assertEquals(expected, remote.url().toString());
    }

    @DataProvider
    public static Object[][] urlProvider()
    {
        return new Object[][] {
            {
                "git@bitbucket.org:foo/bar.git",
                "http://bitbucket.org/foo/bar"
            },
            {
                "https://foo@bitbucket.org/foo/bar",
                "https://foo@bitbucket.org/foo/bar"
            },
            {
                "ssh://git@stash.example.com:7999/foo/bar.git",
                "http://stash.example.com/foo/bar"
            },
            {
                "git://github.com/foo/bar",
                "http://github.com/foo/bar"
            },
        };
    }

    @Test(expected = RemoteException.class)
    public void testDoesThrowWhenRemoteUrlNotFound() throws RemoteException
    {
        GitRemote gitRemote = mock(GitRemote.class);
        Git git = mock(Git.class);

        when(gitRemote.getFirstUrl()).thenReturn(null);

        Remote remote = new Remote(git, gitRemote);

        remote.url();
    }
}
