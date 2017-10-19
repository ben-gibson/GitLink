package uk.co.ben_gibson.git.link.Logger.Handlers;

import com.intellij.openapi.diagnostic.Logger;
import uk.co.ben_gibson.git.link.Logger.LogMessage;

/**
 * Decorates the intellij diagnostic logger.
 */
public class DiagnosticLogHandler implements LogHandler
{
    private Logger logger;

    public DiagnosticLogHandler(Logger logger)
    {
        this.logger = logger;
    }

    public void handle(LogMessage message)
    {
        if (message.error()) {
            this.logger.error(message.toString());
        } else if (message.notice()) {
            this.logger.info(message.toString());
        } else {
            this.logger.warn(message.toString());
        }
    }

    public boolean handles(LogMessage message)
    {
        return true;
    }
}
