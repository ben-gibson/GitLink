package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Preferences;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;

import java.util.Arrays;
import java.util.List;

public class UrlFactoryProvider
{
    private List<UrlFactory> factories;

    public UrlFactoryProvider(List<UrlFactory> factories)
    {
        this.factories = factories;
    }


    /**
     * I have no idea how to source this from the service manager and there's very little documentation around
     * plugin development in general so this has to be called each time the plugin is triggered to build
     * the correct factory provider using the current project's preferences.
     */
    public static UrlFactoryProvider fromPreferences(Preferences preferences)
    {
        CustomUrlFactory customUrlFactory = new CustomUrlFactory(
            preferences.customFileUrlOnBranchTemplate,
            preferences.customFileUrlAtCommitTemplate,
            preferences.customCommitUrlTemplate
        );

        return new UrlFactoryProvider(
            Arrays.asList(
                new GitHubUrlFactory(),
                new GitLabUrlFactory(),
                new BitbucketCloudUrlFactory(),
                new BitbucketServerUrlFactory(),
                new GitBlitUrlFactory(),
                new GogsUrlFactory(),
                customUrlFactory
            )
        );
    }


    public UrlFactory urlFactoryForHost(RemoteHost host) throws UrlFactoryException
    {
        for (UrlFactory factory : this.factories) {
            if (factory.supports(host)) {
                return factory;
            }
        }

        throw UrlFactoryException.unsupportedRemoteHost(host);
    }
}
