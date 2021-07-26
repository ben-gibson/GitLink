package uk.co.ben_gibson.git.link.Url.Factory;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.git.link.Preferences;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;

import java.util.ArrayList;
import java.util.List;

public class UrlFactoryLocator
{
    private List<UrlFactory> factories = new ArrayList<>();
    private Preferences preferences;

    public static UrlFactoryLocator getInstance(Project project) {
        return ServiceManager.getService(project, UrlFactoryLocator.class);
    }

    public UrlFactoryLocator(Project project) {

        this.preferences = Preferences.getInstance(project);

        this.factories.add(ServiceManager.getService(GitLabUrlFactory.class));
        this.factories.add(ServiceManager.getService(AzureUrlFactory.class));
        this.factories.add(ServiceManager.getService(GitHubUrlFactory.class));
        this.factories.add(ServiceManager.getService(GogsUrlFactory.class));
        this.factories.add(ServiceManager.getService(BitbucketServerUrlFactory.class));
        this.factories.add(ServiceManager.getService(BitbucketCloudUrlFactory.class));
        this.factories.add(ServiceManager.getService(project, CustomUrlFactory.class));
    }

    public UrlFactory locate() throws UrlFactoryException {
        for (UrlFactory factory : this.factories) {
            if (factory.supports(this.preferences.getRemoteHost())) {
                return factory;
            }
        }

        throw UrlFactoryException.unsupportedRemoteHost(this.preferences.getRemoteHost());
    }
}
