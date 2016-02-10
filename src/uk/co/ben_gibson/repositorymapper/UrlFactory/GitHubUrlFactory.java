package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;

import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * Creates a URL in the format expected by the remote repository provider GitHub.
 */
public class GitHubUrlFactory implements UrlFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL getUrlFromContext(@NotNull Context context) throws MalformedURLException, URISyntaxException, RemoteRepositoryMapperException, UnsupportedEncodingException
    {

        URL remoteUrl = context.getRepository().getOriginUrl();

        String path = String.format(
            "%s/blob/%s%s",
            remoteUrl.getPath(),
            URLEncoder.encode(context.getBranch(), "UTF-8"),
            context.getRepositoryRelativeFilePath()
        );

        String fragment = null;

        if (context.getCaretLinePosition() != null) {
            fragment = "L" + context.getCaretLinePosition().toString();
        }

        return new URI(remoteUrl.getProtocol(), remoteUrl.getHost(), path, fragment).toURL();
    }
}
