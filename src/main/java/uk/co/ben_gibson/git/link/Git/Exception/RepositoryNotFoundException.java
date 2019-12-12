package uk.co.ben_gibson.git.link.Git.Exception;

import uk.co.ben_gibson.git.link.Exception.GitLinkException;

/**
 * Thrown when the repository could not be found.
 */
public class RepositoryNotFoundException extends GitLinkException
{
    public RepositoryNotFoundException()
    {
        super("Git repository not found, has the root been registered in Preferences → Version Control?");
    }
}
