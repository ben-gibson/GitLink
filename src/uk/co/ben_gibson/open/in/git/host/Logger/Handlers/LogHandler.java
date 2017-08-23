package uk.co.ben_gibson.open.in.git.host.Logger.Handlers;

import uk.co.ben_gibson.open.in.git.host.Logger.LogMessage;

/**
 * Handles a log message in some way.
 */
public interface LogHandler
{
    void handle(LogMessage message);

    boolean handles(LogMessage message); // Can this handler handle the given message.
}
