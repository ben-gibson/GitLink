package uk.co.ben_gibson.git.link.Url.Factory;

import com.intellij.openapi.application.ApplicationManager;
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
        return project.getService(UrlFactoryLocator.class);
    }

    public UrlFactoryLocator(Project project) {

        this.preferences = Preferences.getInstance(project);

        this.factories.add(ApplicationManager.getApplication().getService(GitLabUrlFactory.class));
        this.factories.add(ApplicationManager.getApplication().getService(GitHubUrlFactory.class));
        this.factories.add(ApplicationManager.getApplication().getService(GogsUrlFactory.class));
        this.factories.add(ApplicationManager.getApplication().getService(BitbucketServerUrlFactory.class));
        this.factories.add(ApplicationManager.getApplication().getService(BitbucketCloudUrlFactory.class));
        this.factories.add(project.getService(CustomUrlFactory.class));
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
