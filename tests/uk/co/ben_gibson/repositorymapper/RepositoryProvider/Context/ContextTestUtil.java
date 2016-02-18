package uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context;

import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
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
    public static Context getMockedContext(String remoteURL, String branch, String filePath, String fileName, Integer caretLinePosition)
        throws MalformedURLException, RemoteNotFoundException
    {
        Context context = mock(Context.class, RETURNS_DEEP_STUBS);

        when(context.getFilePathRelativeToRepository()).thenReturn(filePath);
        when(context.getRepository().getOriginUrl()).thenReturn(new URL(remoteURL));
        when(context.getBranch()).thenReturn(branch);
        when(context.getCaretLinePosition()).thenReturn(caretLinePosition);
        when(context.getFile().getName()).thenReturn(fileName);

        return context;
    }
}
