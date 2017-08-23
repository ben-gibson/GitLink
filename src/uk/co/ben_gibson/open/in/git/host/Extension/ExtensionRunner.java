package uk.co.ben_gibson.open.in.git.host.Extension;

import uk.co.ben_gibson.open.in.git.host.Extension.Exception.ExtensionException;
import uk.co.ben_gibson.open.in.git.host.Logger.Logger;
import java.net.URL;
import java.util.List;

public class ExtensionRunner
{
    private Logger logger;
    private List<Extension> extensions;

    public ExtensionRunner(Logger logger, List<Extension> extensions)
    {
        this.logger     = logger;
        this.extensions = extensions;
    }

    public void run(URL remoteUrl) throws ExtensionException
    {
        logger.notice(String.format("Running extensions with url '%s'", remoteUrl.toString()));

        if (this.extensions.isEmpty()) {
            logger.warning("You have no extensions enabled, enable some: Preferences → Open in Git host");
        }

        for (Extension extension: this.extensions) {
            logger.notice(String.format("Running extension '%s'", extension.displayName()));
            extension.run(remoteUrl);
        }
    }
}
