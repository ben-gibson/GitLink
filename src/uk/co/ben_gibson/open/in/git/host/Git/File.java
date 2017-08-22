package uk.co.ben_gibson.open.in.git.host.Git;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Represents a file under version control.
 */
public class File
{
    private String repositoryPath; // the path relative to the repository.
    private VirtualFile file;

    public File(String relativePath, VirtualFile file)
    {
        this.repositoryPath = relativePath;
        this.file         = file;
    }

    public String repositoryPath()
    {
        return this.repositoryPath;
    }

    public String name()
    {
        return this.file.getName();
    }
}
