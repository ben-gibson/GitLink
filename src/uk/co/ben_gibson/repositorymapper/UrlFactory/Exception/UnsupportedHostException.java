package uk.co.ben_gibson.repositorymapper.UrlFactory.Exception;

import uk.co.ben_gibson.repositorymapper.Host.Host;

/**
 * Thrown when an unsupported host is encountered.
 */
public class UnsupportedHostException extends Exception {

    /**
     * Constructor.
     *
     * @param host The unsupported host.
     */
    public UnsupportedHostException(Host host)
    {
        super("Unsupported host " + host.toString());
    }
}
