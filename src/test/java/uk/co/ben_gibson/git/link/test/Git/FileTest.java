package uk.co.ben_gibson.git.link.test.Git;

import junit.framework.TestCase;
import uk.co.ben_gibson.git.link.Git.File;

public class FileTest extends TestCase
{
    public void testReturnsPath()
    {
        File file = file("foo/bar.java", "bar.java");

        assertSame("foo/bar.java", file.path());
    }

    public void testReturnsName()
    {
        File file = file("foo/baz.java", "baz.java");

        assertSame("baz.java", file.name());
    }

    private File file(String path, String name)
    {
        return new File(path, name);
    }
}
