package uk.co.ben_gibson.open.in.git.host.test.RemoteUrlFactory;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.open.in.git.host.Git.*;
import uk.co.ben_gibson.open.in.git.host.Git.Exception.RemoteException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public abstract class RemoteUrlFactoryTest extends TestCase
{
    public abstract RemoteUrlFactory remoteUrlFactory();

    @Test
    @UseDataProvider("commitProvider")
    public void testCanCreateUrlToRemoteCommit(String originUrl, Commit commit, String expected, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException, MalformedURLException
    {
        RemoteUrlFactory factory = this.remoteUrlFactory();

        Remote remote = this.mockRemote(originUrl);

        URL url = factory.createUrlToRemoteCommit(remote, commit, forceSSL);

        assertEquals(expected, url.toString());
    }

    @Test
    @UseDataProvider("fileProvider")
    public void testCanCreateUrlToRemotePath(String originUrl, Branch branch, Integer lineNumber, String path, String expected, boolean forceSSL) throws RemoteUrlFactoryException, RemoteException, MalformedURLException
    {
        RemoteUrlFactory factory = this.remoteUrlFactory();

        Remote remote = this.mockRemote(originUrl);

        URL url = factory.createUrlToRemotePath(remote, branch, path, lineNumber, forceSSL);

        assertEquals(expected, url.toString());
    }

    private Remote mockRemote(String originUrl) throws MalformedURLException, RemoteException
    {
        Remote remote = mock(Remote.class);

        when(remote.url()).thenReturn(new URL(originUrl));

        return remote;
    }

    static Commit mockCommit(String hash)
    {
        Commit commit = mock(Commit.class);

        when(commit.hash()).thenReturn(hash);

        return commit;
    }
}
