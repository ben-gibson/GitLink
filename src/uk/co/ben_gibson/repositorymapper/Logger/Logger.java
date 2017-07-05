package uk.co.ben_gibson.repositorymapper.Logger;

import uk.co.ben_gibson.repositorymapper.Logger.Handler.LogHandler;
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
