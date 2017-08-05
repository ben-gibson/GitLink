package uk.co.ben_gibson.open.in.git.host;

import uk.co.ben_gibson.open.in.git.host.Git.Remote.RemoteHost;

/**
 * Plugin wide settings.
 */
public class Settings
{
    private RemoteHost remoteHost;
    private boolean enableEventLog;
    private boolean forceSSL;

    /**
     * Constructor
     *
     * @param enableEventLog Should logging to the event window be enabled.
     */
    public Settings(RemoteHost remoteHost, boolean enableEventLog, boolean forceSSL)
    {
        this.remoteHost     = remoteHost;
        this.enableEventLog = enableEventLog;
        this.forceSSL       = forceSSL;
    }

    /**
     * Should we force SSL?
     */
    public boolean forceSSL()
    {
        return this.forceSSL;
    }

    /**
     * Should logging to the event window be enabled?
     */
    public boolean enableEventLog()
    {
        return this.enableEventLog;
    }

    /**
     * Get the remote host.
     */
    public RemoteHost getRemoteHost()
    {
        return this.remoteHost;
    }
}
