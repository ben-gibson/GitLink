package uk.co.ben_gibson.git.link.Git.Exception;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;

public class BranchException extends GitLinkException
{
    private BranchException(@NotNull final String message)
    {
        super(message);
    }

    public static BranchException couldNotFetchBranchesFromRemoteRepository(@NotNull final String reason)
    {
        return new BranchException(String.format("Could not fetch branches from the remote repository - '%s'", reason));
    }
}
