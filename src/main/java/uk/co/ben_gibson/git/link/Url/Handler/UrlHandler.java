package uk.co.ben_gibson.git.link.Url.Handler;

import java.net.URL;

/**
 * Handles a URL in someway. For example, opening it in the browser or copy it to the clipboard.
 */
public interface UrlHandler
{
    void handle(URL url);
}
