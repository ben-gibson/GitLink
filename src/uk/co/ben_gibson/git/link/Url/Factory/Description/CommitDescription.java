package uk.co.ben_gibson.git.link.Url.Factory.Description;

import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Remote;

/**
 * Describes a commit that can be used to create a URL.
 */
public class CommitDescription extends GitDescription
{
    private Commit commit;

    public CommitDescription(Remote remote, Commit commit)
    {
        super(remote);

        this.commit = commit;
    }

    public String commitHash()
    {
        return this.commit.hash();
    }
}
