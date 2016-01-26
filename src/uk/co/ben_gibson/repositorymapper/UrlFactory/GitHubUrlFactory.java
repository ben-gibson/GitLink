package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Creates a URL in the format expected by the remote repository provider GitHub.
 */
public class GitHubUrlFactory implements UrlFactory
{

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL getUrlFromContext(@NotNull Context context) throws MalformedURLException, UrlFactoryException
    {

        String branch;

        try {
            branch = URLEncoder.encode(context.getBranch(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UrlFactoryException("Failed to encode path, unsupported encoding");
        }

        String path = context.getRemoteHost().toString() + "/blob/" + branch + context.getPath();

        if (context.getCaretLinePosition() != null) {
            path += "#L" + context.getCaretLinePosition().toString();
        }

        return new URL(path);
    }
}
