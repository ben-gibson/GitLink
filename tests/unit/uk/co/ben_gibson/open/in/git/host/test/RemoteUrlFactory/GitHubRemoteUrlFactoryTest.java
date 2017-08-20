package uk.co.ben_gibson.open.in.git.host.test.RemoteUrlFactory;

import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.GitHubRemoteUrlFactory;

public class GitHubRemoteUrlFactoryTest extends RemoteUrlFactoryTest
{
    @Override
    public RemoteUrlFactory remoteUrlFactory()
    {
        return new GitHubRemoteUrlFactory(true);
    }
}
