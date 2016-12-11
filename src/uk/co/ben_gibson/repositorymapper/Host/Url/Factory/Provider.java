package uk.co.ben_gibson.repositorymapper.Host.Url.Factory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Host.Host;
import uk.co.ben_gibson.repositorymapper.Host.Url.Exception.UnsupportedHostException;

/**
 * Provides the appropriate Url factory depending on the host.
 */
public class Provider
{

    /**
     * Get the Url factory for the given host.
     *
     * @param host The host to return a factory for.
     *
     * @return Factory
     */
    @NotNull
    public Factory getForHost(Host host) throws UnsupportedHostException
    {
        if (host == Host.GIT_HUB || host == Host.GITLAB) {
            return new GitHub();
        } else if (host == Host.STASH) {
            return new Stash();
        }  else if (host == Host.BITBUCKET) {
            return new BitBucket();
        }  else if (host == Host.GITBLIT) {
            return new GitBlit();
        }

        throw new UnsupportedHostException(host);
    }
}
