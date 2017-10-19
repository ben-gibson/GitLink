package uk.co.ben_gibson.git.link.Git;

import com.intellij.vcs.log.VcsFullCommitDetails;

/**
 * A git commit.
 */
public class Commit
{
    private VcsFullCommitDetails details;

    public Commit(VcsFullCommitDetails details)
    {
        this.details = details;
    }

    public String hash()
    {
        return this.details.getId().toString();
    }
}
