package uk.co.ben_gibson.repositorymapper.UrlFactory.Exception;

import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;
import java.net.URL;

/**
 * Thrown when a project is not found from a repositories remote url.
 */
public class ProjectNotFoundException extends RemoteRepositoryMapperException
{

    /**
     * Constructor.
     *
     * @param remoteUrl The repositories remote url from which a project could not found.
     */
    public ProjectNotFoundException(URL remoteUrl)
    {
        super("Project could not be found from the repositories remote url " + remoteUrl.toString());
    }
}
