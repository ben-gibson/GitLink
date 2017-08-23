package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Description;

import uk.co.ben_gibson.open.in.git.host.Git.Remote;

abstract public class RemoteDescription
{
    private Remote remote;

    RemoteDescription(Remote remote)
    {
        this.remote = remote;
    }

    public Remote remote()
    {
        return remote;
    }
}
