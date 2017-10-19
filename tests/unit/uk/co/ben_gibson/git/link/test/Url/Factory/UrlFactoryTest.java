package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.intellij.openapi.vfs.VirtualFile;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;
import java.net.MalformedURLException;
import java.net.URL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public abstract class UrlFactoryTest extends TestCase
{
    public abstract UrlFactory remoteUrlFactory();

    @Test
    @UseDataProvider("commitProvider")
    public void testCanCreateUrlToCommit(CommitDescription description, String expected) throws UrlFactoryException, RemoteException, MalformedURLException
    {
        UrlFactory factory = this.remoteUrlFactory();

        URL url = factory.createUrl(description);

        assertEquals(expected, url.toString());
    }

    @Test
    @UseDataProvider("fileProvider")
    public void testCanCreateUrlToFile(FileDescription description, String expected) throws UrlFactoryException, RemoteException, MalformedURLException
    {
        UrlFactory factory = this.remoteUrlFactory();

        URL url = factory.createUrl(description);

        assertEquals(expected, url.toString());
    }

    static Remote mockRemote(String originUrl) throws MalformedURLException, RemoteException
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

    static File mockFile(String path, String name)
    {
        VirtualFile virtualFile = mock(VirtualFile.class);

        when(virtualFile.getName()).thenReturn(name);
        when(virtualFile.getPath()).thenReturn(path);

        return new File(path, virtualFile);
    }
}
