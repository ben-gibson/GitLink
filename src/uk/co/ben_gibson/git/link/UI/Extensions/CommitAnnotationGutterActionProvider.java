package uk.co.ben_gibson.git.link.UI.Extensions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vcs.annotate.FileAnnotation;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.UI.Action.AnnotationGutter.CommitAnnotationAction;

public class CommitAnnotationGutterActionProvider implements com.intellij.openapi.vcs.annotate.AnnotationGutterActionProvider
{
    @NotNull
    @Override
    public AnAction createAction(@NotNull FileAnnotation annotation)
    {
        return new CommitAnnotationAction(annotation);
    }
}
