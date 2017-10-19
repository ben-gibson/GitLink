package uk.co.ben_gibson.git.link.UI.Action.Vcs;

import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;

public class ClipboardVcsAction extends VcsLogAction
{
    protected String displayName(RemoteHost remoteHost)
    {
        return String.format("Copy %s commit link to clipboard", remoteHost.toString());
    }

    UrlHandler remoteUrlHandler()
    {
        return container().copyToClipboardHandler();
    }
}
