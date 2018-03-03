package uk.co.ben_gibson.git.link.UI.Action.AnnotationGutter;

import com.intellij.openapi.vcs.annotate.FileAnnotation;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Container;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;

public class ClipboardAnnotationAction extends AnnotationAction
{
    public ClipboardAnnotationAction(@NotNull FileAnnotation annotation)
    {
        super(annotation);
    }


    protected String displayName(RemoteHost remoteHost)
    {
        return String.format("Copy %s link to clipboard", remoteHost.toString());
    }


    UrlHandler urlHandler()
    {
        return Container.getInstance().copyToClipboardHandler();
    }
}
