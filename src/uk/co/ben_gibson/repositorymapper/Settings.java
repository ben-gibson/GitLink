package uk.co.ben_gibson.repositorymapper;

/**
 * Plugin wide settings.
 */
public class Settings
{
    private boolean enableEventLog;

    /**
     * Constructor
     *
     * @param enableEventLog Should logging to the event window be enabled.
     */
    public Settings(boolean enableEventLog)
    {
        this.enableEventLog = enableEventLog;
    }

    /**
     * Should logging to the event window be enabled?
     *
     * @return boolean
     */
    public boolean enableEventLog()
    {
        return this.enableEventLog;
    }
}
