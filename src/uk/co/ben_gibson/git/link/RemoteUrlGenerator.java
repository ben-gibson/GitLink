package uk.co.ben_gibson.git.link;

import uk.co.ben_gibson.git.link.Exception.GitLinkException;

import java.net.URL;

public interface RemoteUrlGenerator
{
    URL generateRemoteURL() throws GitLinkException;
}
