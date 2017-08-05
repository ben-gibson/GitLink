package uk.co.ben_gibson.open.in.git.host.Logger.Handlers;

import com.intellij.openapi.diagnostic.Logger;
import uk.co.ben_gibson.open.in.git.host.Logger.LogMessage;

/**
 * Decorates the intellij diagnostic logger.
 */
public class DiagnosticLogHandler implements LogHandler
{
    private Logger logger;

    /**
     * Constructor.
     *
     * @param logger The intellij logger
     */
    public DiagnosticLogHandler(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public void handle(LogMessage message)
    {
        if (message.isError()) {
            this.logger.error(message.toString());
        } else if (message.isNotice()) {
            this.logger.info(message.toString());
        } else { // TODO
            this.logger.warn(message.toString());
        }
    }
}
