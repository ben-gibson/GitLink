package uk.co.ben_gibson.open.in.git.host.Git;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Represents a version controlled file.
 */
public class File
{
    private VirtualFile file;

    public File(VirtualFile file)
    {
        this.file = file;
    }

    public String path() {
        return this.file.getPath();
    }
}
