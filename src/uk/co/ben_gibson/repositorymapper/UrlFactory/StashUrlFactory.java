package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Creates a URL in the format expected by the remote repository provider Stash.
 */
public class StashUrlFactory implements UrlFactory
{

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL getUrlFromContext(@NotNull Context context) throws MalformedURLException, UrlFactoryException {

        URL remoteHost = context.getRemoteHost();

        String[] parts = remoteHost.getPath().split("/", 3);

        if (parts.length < 3) {
            throw new MalformedURLException("Could not find project and repo from path " + context.getRemoteHost().getPath());
        }

        /**
         * If we find more providers need this level of flexibility we could split host, project and repo
         * within Context but for now this will do.
         */
        String project    = parts[1];
        String repository = parts[2];

        String fullPath = String.format(
            "/projects/%s/repos/%s/browse%s",
            project,
            repository,
            context.getPath()
        );

        try {
            fullPath += "?at=" + URLEncoder.encode("refs/heads/" + context.getBranch(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UrlFactoryException("Failed to encode path, unsupported encoding");
        }

        if (context.getCaretLinePosition() != null) {
            fullPath += "#" + context.getCaretLinePosition().toString();
        }

        fullPath = remoteHost.getProtocol() + "://" + remoteHost.getHost() + fullPath;

        return new URL(fullPath);
    }
}
