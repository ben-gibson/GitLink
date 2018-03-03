package uk.co.ben_gibson.git.link.Git.Exception;

import uk.co.ben_gibson.git.link.Exception.Codes;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;

public class BranchException extends GitLinkException
{
    private BranchException(String message, int code)
    {
        super(message, code);
    }

    public static BranchException couldNotFetchBranchesFromRemoteRepository(String reason)
    {
        return new BranchException(
            String.format("Could not fetch branches from the remote repository - '%s'", reason),
            Codes.GIT_COULD_NOT_FETCH_BRANCH_FROM_REMOTE
        );
    }
}
