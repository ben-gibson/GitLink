package uk.co.ben_gibson.git.link.Url.Handler;

import uk.co.ben_gibson.git.link.Url.Handler.Exception.UrlHandlerException;
import java.net.URL;

/**
 * Handles a URL in some way e.g. copies it the clipboard.
 */
public interface UrlHandler
{
    void handle(URL url) throws UrlHandlerException;

    String name();
}
