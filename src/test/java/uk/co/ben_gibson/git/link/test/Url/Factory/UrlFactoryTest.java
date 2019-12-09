package uk.co.ben_gibson.git.link.test.Url.Factory;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import uk.co.ben_gibson.git.link.Url.Factory.UrlFactory;
import java.net.MalformedURLException;
import java.net.URL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public abstract class UrlFactoryTest extends TestCase
{
    @Test
    @UseDataProvider("commitProvider")
    public void testCanCreateUrlToCommit(UrlFactory urlFactory, Remote remote, Commit commit, String expected) throws UrlFactoryException, RemoteException
    {
        URL url = urlFactory.createUrlToCommit(remote, commit);

        assertEquals(expected, url.toString());
    }

    @Test
    @UseDataProvider("fileAtCommitProvider")
    public void testCanCreateUrlToFileAtCommit(UrlFactory urlFactory, Remote remote, File file, Commit commit, LineSelection lineSelection, String expected) throws UrlFactoryException, RemoteException
    {
        URL url = urlFactory.createUrlToFileAtCommit(remote, file, commit, lineSelection);

        assertEquals(expected, url.toString());
    }

    @Test
    @UseDataProvider("fileOnBranchProvider")
    public void testCanCreateUrlToFileOnBranch(
        UrlFactory urlFactory,
        Remote remote,
        File file,
        Branch branch,
        LineSelection lineSelection,
        String expected
    ) throws UrlFactoryException, RemoteException
    {
        URL url = urlFactory.createUrlToFileOnBranch(remote, file, branch, lineSelection);

        assertEquals(expected, url.toString());
    }

    static Remote mockRemote(String originUrl) throws MalformedURLException, RemoteException
    {
        Remote remote = mock(Remote.class);

        when(remote.url()).thenReturn(new URL(originUrl));

        return remote;
    }
}
