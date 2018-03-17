package uk.co.ben_gibson.git.link.UI.Extensions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.vcs.annotate.FileAnnotation;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.UI.Action.AnnotationGutter.BrowserAnnotationAction;

public class BrowserAnnotationGutterActionProvider implements com.intellij.openapi.vcs.annotate.AnnotationGutterActionProvider
{
    @NotNull
    @Override
    public AnAction createAction(@NotNull FileAnnotation annotation)
    {
        return new BrowserAnnotationAction(annotation);
    }
}
