package uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import uk.co.ben_gibson.open.in.git.host.Git.File;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import uk.co.ben_gibson.open.in.git.host.Git.Repository;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.Exception.RemoteUrlFactoryException;

import java.net.URL;

/**
 * Creates remote git urls to Github.
 */
public class GitHubRemoteUrlFactory extends RemoteUrlFactoryAbstract
{
    public GitHubRemoteUrlFactory(boolean forceSSL)
    {
        super(forceSSL);
    }

    protected URL createRemoteUrlToCommit(Repository repository, Commit commit) throws RemoteUrlFactoryException
    {
        URL host = this.createHostUrl(repository);

        String path = String.format("%s/commit/%s", host.getPath(), commit.hash());

        return this.createRemoteUrl(host, path, null);
    }

    protected URL createRemoteUrlToFile(Repository repository, File file, Integer lineNumber) throws RemoteUrlFactoryException
    {
        URL host = this.createHostUrl(repository);

        String fragment = null;

        String path = String.format(
            "%s/blob/%s%s",
            host.getPath(),
            repository.currentBranch(),
            repository.getRelativePath(file)
        );

        if (lineNumber != null) {
            fragment = String.format("L%d", lineNumber);
        }

        return this.createRemoteUrl(host, path, fragment);
    }

    public boolean supports(RemoteHost host)
    {
        return host.gitHub();
    }
}
