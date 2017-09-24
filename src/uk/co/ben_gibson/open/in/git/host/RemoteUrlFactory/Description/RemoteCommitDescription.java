package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description;

import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.Remote;

/**
 * Describes a remote commit that a URL can be created to.
 */
public class RemoteCommitDescription extends RemoteDescription
{
    private Commit commit;

    public RemoteCommitDescription(Remote remote, Commit commit)
    {
        super(remote);

        this.commit = commit;
    }

    public String commitHash()
    {
        return this.commit.hash();
    }
}
