package uk.co.ben_gibson.git.link.Exception;

import org.jetbrains.annotations.NotNull;

/**
 * A base exception from which all exceptions from this plugin should extend!
 */
public abstract class GitLinkException extends Exception
{
    private int code;

    public GitLinkException(@NotNull String message, int code)
    {
        super(message);
        this.code = code;
    }


    public int code()
    {
        return this.code;
    }
}
