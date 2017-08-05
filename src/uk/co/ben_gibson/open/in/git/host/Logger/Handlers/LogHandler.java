package uk.co.ben_gibson.open.in.git.host.Logger.Handlers;

import uk.co.ben_gibson.open.in.git.host.Logger.LogMessage;

/**
 * Extension a log message in some way.
 */
public interface LogHandler
{
    /**
     * Handle a log message.
     *
     * @param message
     */
    void handle(LogMessage message);
}
