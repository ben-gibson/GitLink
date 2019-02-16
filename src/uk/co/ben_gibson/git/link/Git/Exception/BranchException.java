package uk.co.ben_gibson.git.link.Git.Exception;

import uk.co.ben_gibson.git.link.Exception.GitLinkException;

public class BranchException extends GitLinkException
{
    private BranchException(String message)
    {
        super(message);
    }

    public static BranchException couldNotFetchBranchesFromRemoteRepository(String reason)
    {
        return new BranchException(String.format("Could not fetch branches from the remote repository - '%s'", reason));
    }
}
