package uk.co.ben_gibson.repositorymapper.Settings;

/**
 * Represents a remote repository provider host.
 */
public class Host
{

    private String host;

    /**
     * Constructor.
     *
     * @param host The host.
     */
    Host(String host)
    {
        this.host = host;
    }


    @Override
    public String toString() {
        return this.host;
    }
}
