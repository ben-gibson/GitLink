package uk.co.ben_gibson.repositorymapper.Host.Url.Factory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * Creates a URL in a format expected by GitHub.
 */
public class GitHub implements Factory
{

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL createUrl(@NotNull Context context, boolean forceSSL) throws URISyntaxException, UnsupportedEncodingException, MalformedURLException, RemoteNotFoundException {

        URL remoteUrl = context.getRepository().getOriginUrl(forceSSL);

        String path;
        String fragment = null;


        if (context.getCommitHash() != null) {
            path = String.format("%s/commit/%s", remoteUrl.getPath(), context.getCommitHash());
        } else {
            path = String.format(
                "%s/blob/%s%s",
                remoteUrl.getPath(),
                context.getBranch(),
                context.getFilePathRelativeToRepository()
            );

            if (context.getCaretLinePosition() != null) {
                fragment = "L" + context.getCaretLinePosition().toString();
            }
        }

        return new URI(remoteUrl.getProtocol(), remoteUrl.getHost(), path, fragment).toURL();
    }
}
