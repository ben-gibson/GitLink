package uk.co.ben_gibson.repositorymapper.RepositoryProvider.Repository;

import com.intellij.testFramework.UsefulTestCase;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Repository;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Repository test
 */
@RunWith(Parameterized.class)
public class RepositoryTest extends UsefulTestCase
{

    private String remote;
    private String expectedUrl;


    /**
     * Constructor.
     *
     * @param remote       The remote we want a url from.
     * @param expectedUrl  The expected url after normalisation.
     *
     */
    public RepositoryTest(String remote, String expectedUrl)
    {
        this.remote       = remote;
        this.expectedUrl  = expectedUrl;
    }


    /**
     * Tests getting a remotes url.
     */
    @Test
    public void testGetRemoteUrl() throws MalformedURLException, RemoteNotFoundException
    {
        assertEquals(this.expectedUrl, this.getRepository().getOriginUrl().toString());
    }



    /**
     * Acts as a data provider.
     *
     * @return Collection
     */
    @Parameterized.Parameters
    public static Collection contexts() throws MalformedURLException
    {
        return Arrays.asList(new Object[][] {
            {"https://github.com/ben-gibson/remote-repository-mapper.git", "https://github.com/ben-gibson/remote-repository-mapper"}
        });
    }


    /**
     * Get the repository.
     *
     * @return Repository
     */
    public Repository getRepository()
    {
        GitRepository gitRepository = mock(GitRepository.class);

        return new Repository(gitRepository, "master");
    }
}
