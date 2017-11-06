package uk.co.ben_gibson.git.link.test.Git;

import com.intellij.openapi.vfs.VirtualFile;
import junit.framework.TestCase;
import uk.co.ben_gibson.git.link.Git.File;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileTest extends TestCase
{
    public void testReturnsPath()
    {
        File file = file("foo/bar.java", "bar.java");

        assertSame("foo/bar.java", file.pathWithName());
    }

    public void testReturnsName()
    {
        File file = file("foo/baz.java", "baz.java");

        assertSame("baz.java", file.name());
    }

    private File file(String path, String name)
    {
        VirtualFile virtualFile = mock(VirtualFile.class);

        when(virtualFile.getName()).thenReturn(name);

        return new File(path, virtualFile);
    }
}
