package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.UrlFactory.Exception.ProjectNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import java.net.*;

/**
 * Creates a URL in a format expected by Stash.
 */
public class StashUrlFactory implements UrlFactory
{

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public URL createUrl(@NotNull Context context, boolean forceSSL) throws MalformedURLException, URISyntaxException, RemoteNotFoundException, ProjectNotFoundException {
        URL remoteUrl = context.getRepository().getOriginUrl(forceSSL);

        String[] parts = remoteUrl.getPath().split("/", 3);

        if (parts.length < 3) {
            throw new ProjectNotFoundException(remoteUrl);
        }

        String projectName    = parts[1];
        String repositoryName = parts[2];

        String path;
        String query = null;
        String fragment = null;

        if (context.getCommitHash() != null) {
            path = String.format("/projects/%s/repos/%s/commits/%s", projectName, repositoryName, context.getCommitHash());
        } else {
            path = String.format(
                "/projects/%s/repos/%s/browse%s",
                projectName,
                repositoryName,
                context.getFilePathRelativeToRepository()
            );

            query = "at=refs/heads/" + context.getBranch();

            if (context.getCaretLinePosition() != null) {
                fragment = context.getCaretLinePosition().toString();
            }
        }

        return new URI(remoteUrl.getProtocol(), remoteUrl.getHost(), path, query, fragment).toURL();
    }
}
