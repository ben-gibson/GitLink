package uk.co.ben_gibson.git.link.Url.Modifier;

import uk.co.ben_gibson.git.link.Url.Modifier.Exception.ModifierException;
import java.net.URL;

/**
 * Modify a URL in some way.
 */
public interface UrlModifier
{
    URL modify(URL url) throws ModifierException;

    String name();
}
