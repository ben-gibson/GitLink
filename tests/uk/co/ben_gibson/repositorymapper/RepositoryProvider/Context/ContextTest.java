package uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context;

import com.intellij.testFramework.UsefulTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Tests the context.
 */
@RunWith(Parameterized.class)
public class ContextTest extends UsefulTestCase
{

    private String filePath;
    private String repositoryRootPath;
    private String expectedPath;

    /**
     * Constructor.
     *
     * @param filePath            The absolute file path.
     * @param repositoryRootPath  The repository root path.
     * @param expectedPath        The expected file path that is relative to the repository root path.
     *
     */
    public ContextTest(String filePath, String repositoryRootPath, String expectedPath)
    {
        this.filePath     = filePath;
        this.repositoryRootPath = repositoryRootPath;
        this.expectedPath = expectedPath;
    }


    /**
     * Tests the file path is returned relative to the remote repisotries root path.
     */
    public void testGetUrlFromContext()
    {
        //assertEquals(this.expectedPath, this.getContext().getRepositoryRelativeFilePath());
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
            {"/foo/bar/example/src/foo/bar/bar.php", "/foo/bar/example/", "src/foo/bar/bar.php"}
        });
    }
}
