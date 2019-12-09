package uk.co.ben_gibson.git.link.Url.Modifier;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Url.Modifier.Exception.ModifierException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Modifies a URL by forcing the protocol to use HTTPS.
 */
public class HttpsUrlModifier implements UrlModifier
{
    public URL modify(@NotNull URL url) throws ModifierException
    {
        if (url.getProtocol().equals("https")) {
            return url;
        }

        try {

            String file = url.getFile();

            if (url.getRef() != null) {
                file = file.concat("#" + url.getRef());
            }

            return new URL("https", url.getHost(), url.getPort(), file);
        } catch (MalformedURLException e) {
            throw ModifierException.invalidUrlAfterModification(url);
        }
    }

    public String name()
    {
        return "Force HTTPS";
    }
}
