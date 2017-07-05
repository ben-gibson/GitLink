package uk.co.ben_gibson.repositorymapper.Logger.Handler;

import uk.co.ben_gibson.repositorymapper.Logger.LogMessage;

/**
 * Handles a log message in some way.
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
