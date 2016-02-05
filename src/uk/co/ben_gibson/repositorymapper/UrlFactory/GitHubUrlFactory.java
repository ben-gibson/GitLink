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
    public URL getUrlFromContext(@NotNull Context context) throws MalformedURLException, UrlFactoryException, URISyntaxException
    {

        URL remoteUrl = this.getRemoteUrlFromRepository(context.getRepository());

        String path = String.format(
            "%s/blob/%s%s",
            remoteUrl.getPath(),
            this.getBranch(context.getRepository()).getName(),
             context.getRepositoryRelativeFilePath()
        );

        String fragment = null;

        if (context.getCaretLinePosition() != null) {
            fragment = "L" + context.getCaretLinePosition().toString();
        }

        return new URI(remoteUrl.getProtocol(), remoteUrl.getHost(), path, fragment).toURL();
    }
}
