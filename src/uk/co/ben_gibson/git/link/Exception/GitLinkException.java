package uk.co.ben_gibson.git.link.Exception;

import org.jetbrains.annotations.NotNull;

/**
 * A base exception from which all exceptions from this plugin should extend!
 */
public class GitLinkException extends Exception
{
    public GitLinkException(@NotNull String message)
    {
        super(message);
    }
}
