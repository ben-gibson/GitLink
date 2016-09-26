package uk.co.ben_gibson.repositorymapper.Host.Url.Factory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Creates a URL in a format expected by BitBucket.
 */
public class BitBucket implements Factory
{

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL createUrl(@NotNull Context context, boolean forceSSL) throws MalformedURLException, URISyntaxException, RemoteNotFoundException {

        URL remoteUrl = context.getRepository().getOriginUrl(forceSSL);

        String path;
        String query = null;
        String fragment = null;

        if (context.getCommitHash() != null) {
            path = String.format("%s/commits/%s", remoteUrl.getPath(), context.getCommitHash());
        } else {
            path = String.format(
                "%s/src/%s%s",
                remoteUrl.getPath(),
                "HEAD",
                context.getFilePathRelativeToRepository()
            );

            query = "at=" + context.getBranch();

            if (context.getCaretLinePosition() != null) {
                fragment = context.getFile().getName() + "-" + context.getCaretLinePosition().toString();
            }
        }

        return new URI(remoteUrl.getProtocol(), remoteUrl.getHost(), path, query, fragment).toURL();
    }
}
