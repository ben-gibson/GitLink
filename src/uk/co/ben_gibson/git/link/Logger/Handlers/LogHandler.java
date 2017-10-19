package uk.co.ben_gibson.git.link.Logger.Handlers;

import uk.co.ben_gibson.git.link.Logger.LogMessage;

/**
 * Handles a log message in some way.
 */
public interface LogHandler
{
    void handle(LogMessage message);

    boolean handles(LogMessage message); // Can this handler handle the given message.
}
