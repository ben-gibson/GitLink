package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import java.net.*;

/**
 * Creates a URL in the format expected by the remote repository provider Bitbucket.
 */
public class BitBucketUrlFactory implements UrlFactory {


    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL getUrlFromContext(@NotNull Context context) throws MalformedURLException, UrlFactoryException, URISyntaxException
    {

        URL remoteUrl = context.getRepository().getRemoteUrlFromRepository();

        String path = String.format(
            "%s/src/%s%s",
            remoteUrl.getPath(),
            "HEAD",
            context.getRepositoryRelativeFilePath()
        );

        String query = "at=" + context.getRepository().getActiveBranchNameWithRemote();

        String fragment = null;

        if (context.getCaretLinePosition() != null) {
            fragment = context.getFile().getName() + "-" + context.getCaretLinePosition().toString();
        }

        return new URI(remoteUrl.getProtocol(), remoteUrl.getHost(), path, query, fragment).toURL();
    }
}
