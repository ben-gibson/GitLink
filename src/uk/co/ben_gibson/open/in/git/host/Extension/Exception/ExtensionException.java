package uk.co.ben_gibson.open.in.git.host.Extension.Exception;

import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import uk.co.ben_gibson.open.in.git.host.OpenInGitHostException;

import java.net.URL;

public class ExtensionException extends OpenInGitHostException
{
    public ExtensionException(String message)
    {
        super(message);
    }

    public static ExtensionException couldNotHandleRemoteUrl(Extension extension, URL remoteUrl)
    {
        return new ExtensionException(String.format(
            "Extension '%s' could not handle the remote URL '%s'",
            extension.displayName(),
            remoteUrl.toString()
        ));
    }
}
