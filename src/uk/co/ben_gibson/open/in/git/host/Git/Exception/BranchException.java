package uk.co.ben_gibson.open.in.git.host.Git.Exception;

import uk.co.ben_gibson.open.in.git.host.Exception.OpenInGitHostException;

public class BranchException extends OpenInGitHostException
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
