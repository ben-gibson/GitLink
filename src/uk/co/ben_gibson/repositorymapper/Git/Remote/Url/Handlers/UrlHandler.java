package uk.co.ben_gibson.repositorymapper.Git.Remote.Url.Handlers;

import uk.co.ben_gibson.repositorymapper.Git.Remote.Url.RemoteUrl;

/**
 * Handles a git host url in some way.
 */
public interface UrlHandler
{
    /**
     * Handle a git host url in some way.
     *
     * @param hostUrl The host url to handle.
     */
    void handle(RemoteUrl hostUrl);
}
