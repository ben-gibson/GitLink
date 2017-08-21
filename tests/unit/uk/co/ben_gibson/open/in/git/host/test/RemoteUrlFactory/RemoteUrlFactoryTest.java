package uk.co.ben_gibson.open.in.git.host.test.RemoteUrlFactory;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.Git.File;
import uk.co.ben_gibson.open.in.git.host.Git.Remote;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import java.net.MalformedURLException;
import java.net.URL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public abstract class RemoteUrlFactoryTest extends TestCase
{
    public abstract RemoteUrlFactory remoteUrlFactory();

    @Test
    @UseDataProvider("commitProvider")
    public void testCanCreateRemoteUrlToCommit(String originUrl, Commit commit, String expected, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException, MalformedURLException
    {
        RemoteUrlFactory factory = this.remoteUrlFactory();

        Repository repository = this.mockRepository(null, originUrl);

        URL url = factory.createRemoteUrlToCommit(repository, commit, forceSSL);

        assertEquals(expected, url.toString());
    }

    @Test
    @UseDataProvider("fileProvider")
    public void testCanCreateRemoteUrlToFile(String branch, String originUrl, Integer lineNumber, File file, String expected, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException, MalformedURLException
    {
        RemoteUrlFactory factory = this.remoteUrlFactory();

        Repository repository = this.mockRepository(branch, originUrl);

        URL url = factory.createRemoteUrlToFile(repository, file, lineNumber, forceSSL);

        assertEquals(expected, url.toString());
    }

    public void testCanCreateRemoteUrlToFile()
    {
        assertTrue(true);
    }

    private Repository mockRepository(String branch, String originUrl) throws RemoteException, MalformedURLException
    {
        if (branch == null) {
            branch = "master";
        }

        Repository repository = mock(Repository.class);
        Remote origin         = mock(Remote.class);

        when(repository.currentBranch()).thenReturn(branch);
        when(repository.origin()).thenReturn(origin);
        when(origin.url()).thenReturn(new URL(originUrl));

        return repository;
    }

    static Commit mockCommit(String hash)
    {
        Commit commit = mock(Commit.class);

        when(commit.hash()).thenReturn(hash);

        return commit;
    }

    static File mockFile(String path)
    {
        File file = mock(File.class);

        when(file.path()).thenReturn(path);

        return file;
    }
}
