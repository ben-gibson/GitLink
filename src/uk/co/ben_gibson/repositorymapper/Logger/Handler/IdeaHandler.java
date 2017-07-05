package uk.co.ben_gibson.repositorymapper.Logger.Handler;

import com.intellij.openapi.diagnostic.Logger;
import uk.co.ben_gibson.repositorymapper.Logger.LogMessage;

/**
 * Decorates the intellij diagnostic logger.
 */
public class IdeaHandler implements LogHandler
{
    private Logger logger;

    /**
     * Constructor.
     *
     * @param logger The intellij logger
     */
    public IdeaHandler(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public void handle(LogMessage message)
    {
        this.logger.error(message.toString());
    }
}
