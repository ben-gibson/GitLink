package uk.co.ben_gibson.repositorymapper.RepositoryProvider.Repository;

import com.intellij.testFramework.UsefulTestCase;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import git4idea.commands.Git;
import git4idea.repo.GitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Remote;
import uk.co.ben_gibson.repositorymapper.Repository.Repository;
import java.net.MalformedURLException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the repository.
 */
@RunWith(DataProviderRunner.class)
public class RepositoryTest extends UsefulTestCase
{

    /**
     * Tests remote url is returned in the canonical form.
     */
    @Test
    @UseDataProvider("getRemoteUrls")
    public void testGetRemoteUrl(Remote remote, String canonicalURL, boolean forceSSL) throws MalformedURLException, RemoteNotFoundException
    {
        assertEquals(canonicalURL, this.getRepository().getRemoteUrl(remote, forceSSL).toString());
    }


    /**
     * Tests origin not found is thrown.
     */
    @Test(expected=RemoteNotFoundException.class)
    public void testOriginNotFoundException() throws MalformedURLException, RemoteNotFoundException
    {
        this.getRepository().getOriginUrl(true);
    }


    /**
     * Acts as a data provider for remote urls.
     *
     * @return Object[][]
     */
    @DataProvider
    public static Object[][] getRemoteUrls() throws RemoteNotFoundException
    {
        return new Object[][] {
            {getMockedRemote("https://bitbucket.org/foo/bar.git"), "https://bitbucket.org/foo/bar", false},
            {getMockedRemote("ssh://git@stash.example.com:7999/foo/bar.git"), "https://stash.example.com/foo/bar", true},
            {getMockedRemote("https://github.com/foo/bar.git"), "https://github.com/foo/bar", true},
            {getMockedRemote("git@bitbucket.org:foo/bar.git"), "http://bitbucket.org/foo/bar", false},
            {getMockedRemote("https://foo@bitbucket.org/foo/bar"), "https://foo@bitbucket.org/foo/bar", true},
            {getMockedRemote("git://github.com/foo/bar"), "https://github.com/foo/bar", true},
        };
    }


    /**
     * Get repository.
     *
     * @return Repository
     */
    public Repository getRepository()
    {
        return new Repository(mock(Git.class), mock(GitRepository.class), "master");
    }


    /**
     * Get mocked remote.
     *
     * @return Remote
     */
    public static Remote getMockedRemote(String firstURL) throws RemoteNotFoundException
    {
        Remote remote = mock(Remote.class);
        when(remote.getFirstUrl()).thenReturn(firstURL);

        return remote;
    }
}
