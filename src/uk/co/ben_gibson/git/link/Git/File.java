package uk.co.ben_gibson.git.link.Git;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Represents a file under version control.
 */
public class File
{
    private String path; // A pathWithName from the root of the repository.
    private VirtualFile file; // The underlying file.

    public File(String path, VirtualFile file)
    {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        this.path = path;
        this.file = file;
    }

    public String path()
    {
        return this.path.substring(0, (this.path.length() - this.name().length()));
    }

    public String pathWithName()
    {
        return this.path;
    }

    public String name()
    {
        return this.file.getName();
    }
}
