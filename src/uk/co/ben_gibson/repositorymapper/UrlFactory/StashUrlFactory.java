package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import java.net.*;

/**
 * Creates a URL in the format expected by the remote repository provider Stash.
 */
public class StashUrlFactory extends AbstractUrlFactory
{

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL getUrlFromContext(@NotNull Context context) throws MalformedURLException, UrlFactoryException, URISyntaxException {

        URL remoteHost = this.getRemoteHostFromRepository(context.getRepository());

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

        String path = String.format(
            "/projects/%s/repos/%s/browse%s",
            project,
            repository,
            this.getRepositoryRelativeFilePath(context.getRepository(), context.getFile())
        );

        String query = "at=refs/heads/" + context.getBranch().getName();

        String fragment = null;

        if (context.getCaretLinePosition() != null) {
            fragment = context.getCaretLinePosition().toString();
        }

        return new URI(remoteHost.getProtocol(), remoteHost.getHost(), path, query, fragment).toURL();
    }
}
