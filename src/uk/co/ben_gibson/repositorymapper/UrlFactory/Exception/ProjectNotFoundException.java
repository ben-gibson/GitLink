package uk.co.ben_gibson.repositorymapper.UrlFactory.Exception;

import java.net.URL;

/**
 * Thrown when the expected project id is not found in the url.
 */
public class ProjectNotFoundException extends Exception {

    /**
     * Constructor.
     *
     * @param remoteUrl The url from which a project could not found.
     */
    public ProjectNotFoundException(URL remoteUrl)
    {
        super("Project could not be found from the repositories remote url " + remoteUrl.toString());
    }
}
