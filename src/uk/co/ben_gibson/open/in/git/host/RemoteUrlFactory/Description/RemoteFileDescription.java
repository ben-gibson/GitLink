package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description;

import uk.co.ben_gibson.open.in.git.host.Git.Branch;
import uk.co.ben_gibson.open.in.git.host.Git.File;
import uk.co.ben_gibson.open.in.git.host.Git.Remote;

/**
 * Describes a remote file that a URL can be created to.
 */
public class RemoteFileDescription extends RemoteDescription
{
    private Branch branch;
    private File file;
    private Integer lineNumber;

    public RemoteFileDescription(Remote remote, Branch branch, File file, Integer lineNumber)
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
