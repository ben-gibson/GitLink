package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class BitBucketUrlFactory implements UrlFactory
{

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL getUrlFromContext(@NotNull Context context, boolean forceSSL) throws MalformedURLException, URISyntaxException, RemoteRepositoryMapperException
    {

        URL remoteUrl = context.getRepository().getOriginUrl(forceSSL);

        String path = String.format(
            "%s/src/%s%s",
            remoteUrl.getPath(),
            "HEAD",
            context.getFilePathRelativeToRepository()
        );

        String query = "at=" + context.getBranch();

        String fragment = null;

        if (context.getCaretLinePosition() != null) {
            fragment = context.getFile().getName() + "-" + context.getCaretLinePosition().toString();
        }

        return new URI(remoteUrl.getProtocol(), remoteUrl.getHost(), path, query, fragment).toURL();
    }
}
