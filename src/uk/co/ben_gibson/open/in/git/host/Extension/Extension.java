package uk.co.ben_gibson.open.in.git.host.Extension;

import uk.co.ben_gibson.open.in.git.host.Git.Remote.Url.RemoteUrl;

/**
 * An extension of the plugin.
 */
public interface Extension
{
    /**
     *  Handle a remote url.
     *
     * @param url The remote url to handle.
     */
    void handle(RemoteUrl url);
}
