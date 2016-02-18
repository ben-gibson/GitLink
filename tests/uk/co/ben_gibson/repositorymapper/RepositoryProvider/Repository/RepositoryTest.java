package uk.co.ben_gibson.repositorymapper.RepositoryProvider.Repository;

import com.intellij.testFramework.UsefulTestCase;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
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
    public void testGetRemoteUrl(Remote remote, String canonicalizedURL) throws MalformedURLException, RemoteNotFoundException
    {
        assertEquals(canonicalizedURL, this.getRepository().getRemoteUrl(remote).toString());
    }


    /**
     * Tests origin not found is thrown.
     */
    @Test(expected=RemoteNotFoundException.class)
    public void testOriginNotFoundException() throws MalformedURLException, RemoteNotFoundException
    {
        this.getRepository().getOriginUrl();
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
            {getMockedRemote("https://bitbucket.org/foo/bar.git"), "https://bitbucket.org/foo/bar"},
            {getMockedRemote("ssh://git@stash.example.com:7999/foo/bar.git"), "https://stash.example.com/foo/bar"},
            {getMockedRemote("https://github.com/foo/bar.git"), "https://github.com/foo/bar"},
            {getMockedRemote("git@bitbucket.org:foo/bar.git"), "https://bitbucket.org/foo/bar"},
            {getMockedRemote("https://foo@bitbucket.org/foo/bar"), "https://foo@bitbucket.org/foo/bar"},
        };
    }


    /**
     * Get repository.
     *
     * @return Repository
     */
    public Repository getRepository()
    {
        return new Repository(mock(GitRepository.class), "master");
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
