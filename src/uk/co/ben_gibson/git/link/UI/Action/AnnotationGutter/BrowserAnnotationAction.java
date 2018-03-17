package uk.co.ben_gibson.git.link.UI.Action.AnnotationGutter;

import com.intellij.openapi.vcs.annotate.FileAnnotation;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Container;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;

public class BrowserAnnotationAction extends AnnotationAction
{
    public BrowserAnnotationAction(@NotNull FileAnnotation annotation)
    {
        super(annotation);
    }


    protected String displayName(RemoteHost remoteHost)
    {
        return String.format("Open in %s", remoteHost.toString());
    }


    UrlHandler urlHandler()
    {
        return Container.getInstance().openInBrowserHandler();
    }
}
