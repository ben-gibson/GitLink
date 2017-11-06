package uk.co.ben_gibson.git.link.Url.Factory.Description;

import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Git.File;

/**
 * Describes a file that can be used to create a URL.
 */
public class FileDescription extends GitDescription
{
    private Branch branch;
    private File file;
    private Integer lineNumber;

    public FileDescription(Remote remote, Branch branch, File file, Integer lineNumber)
    {
        super(remote);

        this.branch     = branch;
        this.file       = file;
        this.lineNumber = lineNumber;
    }

    public Branch branch()
    {
        return this.branch;
    }

    public File file()
    {
        return this.file;
    }

    public Integer lineNumber()
    {
        return this.lineNumber;
    }

    public boolean hasLineNumber()
    {
        return (this.lineNumber != null);
    }
}
