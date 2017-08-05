package uk.co.ben_gibson.open.in.git.host.Logger;

import uk.co.ben_gibson.open.in.git.host.Logger.Handlers.LogHandler;
import uk.co.ben_gibson.open.in.git.host.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Logs a message to all registered handles
 */
public class Logger
{
    private List<LogHandler> handlers = new ArrayList<LogHandler>();

    /**
     * Logs a notice.
     *
     * @param message The notice message to log.
     */
    public void notice(String message)
    {
        this.log(LogMessage.notice(message));
    }

    /**
     * Logs an error.
     *
     * @param message The error message to log.
     */
    public void error(String message)
    {
        this.log(LogMessage.error(message));
    }

    /**
     * Register a handler.
     *
     * @param handler The handler to register.
     */
    public void registerHandler(LogHandler handler)
    {
        this.handlers.add(handler);
    }

    /**
     * Logs a message.
     *
     * @param message The message to log.
     */
    private void log(LogMessage message)
    {
        for (LogHandler handler : this.handlers) {
            handler.handle(message);
        }
    }
}
