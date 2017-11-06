package uk.co.ben_gibson.git.link.Url.Factory.Description;

import uk.co.ben_gibson.git.link.Git.Remote;

abstract public class GitDescription
{
    private Remote remote;

    GitDescription(Remote remote)
    {
        this.remote = remote;
    }

    public Remote remote()
    {
        return remote;
    }
}
