package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import java.net.*;

/**
 * Creates a URL in the format expected by the remote repository provider GitHub.
 */
public class GitHubUrlFactory extends AbstractUrlFactory
{

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL getUrlFromContext(@NotNull Context context) throws MalformedURLException, UrlFactoryException, URISyntaxException {

        URL remoteHost = context.getRemoteHost();

        String path = context.getRemoteHost().getPath() + "/blob/" + context.getBranch() + context.getPath();

        String fragment = null;

        if (context.getCaretLinePosition() != null) {
            fragment = "L" + context.getCaretLinePosition().toString();
        }

        return new URI(remoteHost.getProtocol(), remoteHost.getHost(), path, fragment).toURL();
    }
}
