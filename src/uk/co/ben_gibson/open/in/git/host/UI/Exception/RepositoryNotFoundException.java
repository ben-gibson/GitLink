package uk.co.ben_gibson.open.in.git.host.UI.Exception;

import uk.co.ben_gibson.open.in.git.host.Exception.InvalidConfigurationException;
import uk.co.ben_gibson.open.in.git.host.Exception.OpenInGitHostException;

/**
 * Thrown when the git repository could not be found.
 */
public class RepositoryNotFoundException extends OpenInGitHostException implements InvalidConfigurationException
{
    public RepositoryNotFoundException()
    {
        super("Git repository not found, make sure you have registered your version control root: Preferences → Version Control");
    }
}
