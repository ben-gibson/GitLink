package uk.co.ben_gibson.repositorymapper.RepositoryProvider.Context;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.UsefulTestCase;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.BranchNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Repository;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the context.
 */
@RunWith(DataProviderRunner.class)
public class ContextTest extends UsefulTestCase
{


    /**
     * Tests the file path is returned relative to the repository path.
     */
    @Test
    @UseDataProvider("getPaths")
    public void testFilePathIsRelativeToRepository(String filePath, String repositoryRootPath, String expectedPath)
    {
        Repository repository = mock(Repository.class, RETURNS_DEEP_STUBS);
        VirtualFile file      = mock(VirtualFile.class, RETURNS_DEEP_STUBS);

        when(repository.getRoot().getPath()).thenReturn(repositoryRootPath);
        when(file.getPath()).thenReturn(filePath);

        Context context = this.getContext(repository, file);

        assertEquals(expectedPath, context.getFilePathRelativeToRepository());
    }


    /**
     * Tests getting a branch.
     */
    @Test
    public void testGetBranch() throws BranchNotFoundException
    {
        Repository repository = mock(Repository.class, RETURNS_DEEP_STUBS);
        VirtualFile file      = mock(VirtualFile.class, RETURNS_DEEP_STUBS);

        when(repository.getActiveBranchWithRemote()).thenReturn("fix-some-issue");

        assertEquals("fix-some-issue", this.getContext(repository, file).getBranch());
    }


    /**
     * Tests the default branch is used as a fallback.
     */
    @Test
    public void testGetBranchFallsBackToDefault() throws BranchNotFoundException
    {
        Repository repository = mock(Repository.class, RETURNS_DEEP_STUBS);
        VirtualFile file      = mock(VirtualFile.class, RETURNS_DEEP_STUBS);

        when(repository.getActiveBranchWithRemote()).thenThrow(BranchNotFoundException.activeBranchWithRemoteTrackingNotFound());
        when(repository.getDefaultBranch()).thenReturn("master");

        assertEquals("master", this.getContext(repository, file).getBranch());
    }


    /**
     * Date provider for file paths.
     *
     * @return Collection
     */
    @DataProvider
    public static Object[][] getPaths()
    {
        return new Object[][] {
            {"/foo/bar/example/src/foo/bar/bar.php", "/foo/bar/example/", "src/foo/bar/bar.php"}
        };
    }


    /**
     * Get context.
     *
     * @return Context
     */
    public Context getContext(Repository repository, VirtualFile file)
    {
        return new Context(repository, file, null);
    }
}
