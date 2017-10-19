package uk.co.ben_gibson.git.link.UI.Action.Menu;

import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;

public class BrowserMenuAction extends MenuAction
{
    protected String displayName(RemoteHost remoteHost)
    {
        return String.format("Open in %s", remoteHost.toString());
    }

    UrlHandler remoteUrlHandler()
    {
        return container().openInBrowserHandler();
    }
}
