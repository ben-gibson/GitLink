package uk.co.ben_gibson.open.in.git.host.Action.Exception;

import uk.co.ben_gibson.open.in.git.host.OpenInGitHostException;

public class ActionException extends OpenInGitHostException
{
    private ActionException(String message)
    {
        super(message);
    }

    public static ActionException fileNotFound()
    {
        return new ActionException("File not found");
    }

    public static ActionException vcsLogNotFound()
    {
        return new ActionException("VCS log not found");
    }
}
