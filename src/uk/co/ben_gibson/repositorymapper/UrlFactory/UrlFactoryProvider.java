package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Host.Host;
import uk.co.ben_gibson.repositorymapper.UrlFactory.Exception.UnsupportedHostException;

/**
 * Provides the appropriate Url factory depending on the host.
 */
public class UrlFactoryProvider
{

    /**
     * Get the Url factory for the given host.
     *
     * @param host The host to return a factory for.
     *
     * @return UrlFactory
     */
    @NotNull
    public UrlFactory getForHost(Host host) throws UnsupportedHostException
    {
        if (host == Host.GIT_HUB || host == Host.GITLAB) {
            return new GitHubUrlFactory();
        } else if (host == Host.STASH) {
            return new StashUrlFactory();
        }  else if (host == Host.BITBUCKET) {
            return new BitBucketUrlFactory();
        }  else if (host == Host.GITBLIT) {
            return new GitBlitUrlFactory();
        }

        throw new UnsupportedHostException(host);
    }
}
