package uk.co.ben_gibson.repositorymapper.Logger.Handlers;

import uk.co.ben_gibson.repositorymapper.Logger.LogMessage;

/**
 * Handlers a log message in some way.
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
