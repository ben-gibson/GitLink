package uk.co.ben_gibson.open.in.git.host.Logger;

import uk.co.ben_gibson.open.in.git.host.Exception.InvalidConfigurationException;
import uk.co.ben_gibson.open.in.git.host.Logger.Handlers.LogHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Logs a message to all registered handles
 */
public class Logger
{
    private List<LogHandler> handlers = new ArrayList<>();

    public void notice(String message)
    {
        this.log(LogMessage.notice(message));
    }

    public void warning(String message)
    {
        this.log(LogMessage.warning(message));
    }

    public void error(String message)
    {
        this.log(LogMessage.error(message));
    }

    public void exception(Exception exception)
    {
        if (exception instanceof InvalidConfigurationException) {
            this.log(LogMessage.warning(exception.getMessage()));
        } else {
            this.log(LogMessage.error(exception.getMessage()));
        }
    }

    public void registerHandler(LogHandler handler)
    {
        this.handlers.add(handler);
    }

    private void log(LogMessage message)
    {
        for (LogHandler handler : this.handlers) {
            if (handler.handles(message)) {
                handler.handle(message);
            }
        }
    }
}
