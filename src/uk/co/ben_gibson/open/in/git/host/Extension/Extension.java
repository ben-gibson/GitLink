package uk.co.ben_gibson.open.in.git.host.Extension;

import uk.co.ben_gibson.open.in.git.host.Extension.Exception.ExtensionException;

import java.net.URL;

/**
 * An extension of the plugin.
 */
public interface Extension
{
    void run(URL remoteUrl) throws ExtensionException;

    String displayName();
}
