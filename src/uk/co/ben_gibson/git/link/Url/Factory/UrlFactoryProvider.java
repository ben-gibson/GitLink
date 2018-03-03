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


    public static UrlFactoryProvider fromPrefernces(Preferences preferences)
    {
        return new UrlFactoryProvider(
            Arrays.asList(
                new GitHubUrlFactory(),
                new BitBucketUrlFactory(),
                new StashUrlFactory(),
                new GitBlitUrlFactory(),
                new CustomUrlFactory(
                    preferences.customFileUrlOnBranchTemplate,
                    preferences.customFileUrlAtCommitTemplate,
                    preferences.customCommitUrlTemplate
                )
            )
        );
    }


    public UrlFactory forRemoteHost(RemoteHost host) throws UrlFactoryException
    {
        for (UrlFactory factory : this.factories) {
            if (factory.supports(host)) {
                return factory;
            }
        }

        throw UrlFactoryException.unsupportedRemoteHost(host);
    }
}
