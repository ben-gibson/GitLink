package uk.co.ben_gibson.git.link.Url.Factory;

import com.intellij.openapi.components.ServiceManager;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Preferences;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

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
        URLTemplateProcessor urlTemplateProcessor = ServiceManager.getService(URLTemplateProcessor.class);

        CustomUrlFactory customUrlFactory = new CustomUrlFactory(
            urlTemplateProcessor,
            preferences.customFileUrlOnBranchTemplate,
            preferences.customFileUrlAtCommitTemplate,
            preferences.customCommitUrlTemplate
        );

        return new UrlFactoryProvider(
            Arrays.asList(
                new GitHubUrlFactory(urlTemplateProcessor),
                new GitLabUrlFactory(urlTemplateProcessor),
                new BitbucketCloudUrlFactory(urlTemplateProcessor),
                new BitbucketServerUrlFactory(urlTemplateProcessor),
                new GitBlitUrlFactory(),
                new GogsUrlFactory(urlTemplateProcessor),
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
