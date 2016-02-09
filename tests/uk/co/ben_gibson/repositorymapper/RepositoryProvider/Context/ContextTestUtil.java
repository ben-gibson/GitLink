package uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context;

import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.UrlFactory.UrlFactoryException;
import java.net.MalformedURLException;
import java.net.URL;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Utility class for working with a context in a test.
 */
public class ContextTestUtil
{

    /**
     * Get a mocked context.
     *
     * @return Context
     */
    public static Context getMockedContext(String remoteURL, String branch, String filePath)
        throws MalformedURLException, UrlFactoryException
    {
       return getMockedContext(remoteURL, branch, filePath, null);
    }


    /**
     * Get a mocked context.
     *
     * @return Context
     */
    public static Context getMockedContext(String remoteURL, String branch, String filePath, Integer caretLinePosition)
        throws MalformedURLException, UrlFactoryException
    {
        Context context = mock(Context.class, RETURNS_DEEP_STUBS);

        when(context.getRepositoryRelativeFilePath()).thenReturn(filePath);
        when(context.getRepository().getRemoteUrlFromRepository()).thenReturn(new URL(remoteURL));
        when(context.getRepository().getActiveBranchNameWithRemote()).thenReturn(branch);
        when(context.getCaretLinePosition()).thenReturn(caretLinePosition);

        return context;
    }
}
