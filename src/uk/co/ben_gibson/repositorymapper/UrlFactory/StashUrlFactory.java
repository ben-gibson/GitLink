package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;
import uk.co.ben_gibson.repositorymapper.UrlFactory.Exception.ProjectNotFoundException;
import java.net.*;

/**
 * Creates a URL in the format expected by the remote repository provider Stash.
 */
public class StashUrlFactory implements UrlFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL getUrlFromContext(@NotNull Context context) throws MalformedURLException, URISyntaxException, RemoteRepositoryMapperException
    {

        URL remoteUrl = context.getRepository().getOriginUrl();

        String[] parts = remoteUrl.getPath().split("/", 3);

        if (parts.length < 3) {
            throw new ProjectNotFoundException(remoteUrl);
        }

        String projectName    = parts[1];
        String repositoryName = parts[2];

        String path = String.format(
            "/projects/%s/repos/%s/browse%s",
            projectName,
            repositoryName,
            context.getFilePathRelativeToRepository()
        );

        String query = "at=refs/heads/" + context.getBranch();

        String fragment = null;

        if (context.getCaretLinePosition() != null) {
            fragment = context.getCaretLinePosition().toString();
        }

        return new URI(remoteUrl.getProtocol(), remoteUrl.getHost(), path, query, fragment).toURL();
    }
}
