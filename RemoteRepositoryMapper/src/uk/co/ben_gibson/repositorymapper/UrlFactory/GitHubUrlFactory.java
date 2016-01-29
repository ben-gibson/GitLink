package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Creates a URL in the format expected by the remote repository provider GitHub.
 */
public class GitHubUrlFactory implements UrlFactory
{

    @Override
    @NotNull
    public URL getUrlFromContext(Context context) throws MalformedURLException
    {
        String fullPath = String.format("/%s/%s/%s", context.getProject(), context.getRepository(), context.getPath());

        if (context.getCaretLinePosition() != null) {
            fullPath += "#L" + context.getCaretLinePosition().toString();
        }

        return new URL(context.getHost(), fullPath);
    }
}
