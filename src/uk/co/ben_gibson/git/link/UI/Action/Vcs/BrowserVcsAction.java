package uk.co.ben_gibson.git.link.UI.Action.Vcs;

import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;

public class BrowserVcsAction extends VcsLogAction
{
    protected String displayName(RemoteHost remoteHost)
    {
        return String.format("Open commit in %s", remoteHost.toString());
    }

    UrlHandler remoteUrlHandler()
    {
        return container().openInBrowserHandler();
    }
}
