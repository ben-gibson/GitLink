package uk.co.ben_gibson.open.in.git.host.UI.Action.Exception;

import uk.co.ben_gibson.open.in.git.host.Exception.OpenInGitHostException;

public class ActionException extends OpenInGitHostException
{
    private ActionException(String message)
    {
        super(message);
    }

    public static ActionException fileNotFound()
    {
        return new ActionException("File not be found from action event");
    }

    public static ActionException vcsLogNotFound()
    {
        return new ActionException("VCS log not be found from action event");
    }
}
