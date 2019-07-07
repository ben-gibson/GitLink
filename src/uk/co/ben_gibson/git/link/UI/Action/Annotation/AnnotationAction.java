package uk.co.ben_gibson.git.link.UI.Action.Annotation;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.annotate.FileAnnotation;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.UI.Action.Action;
import uk.co.ben_gibson.git.link.UI.LineSelection;

public abstract class AnnotationAction extends Action {

    private FileAnnotation annotation;
    private int lineNumber = -1;

    public AnnotationAction(@NotNull FileAnnotation annotation) {
        this.annotation = annotation;
    }

    protected abstract void perform(
        @NotNull Project project,
        @NotNull Commit commit,
        @NotNull VirtualFile file,
        @NotNull LineSelection lineSelection
    );

    public void actionPerformed(Project project, AnActionEvent event) {

        if (lineNumber < 0) {
            return;
        }

        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);

        VcsRevisionNumber revisionNumber = this.annotation.getLineRevisionNumber(lineNumber);

        if (file == null || project == null || revisionNumber == null) {
            return;
        }

        perform(project, new Commit(revisionNumber.asString()), file, new LineSelection(this.lineNumber + 1));
    }


    protected boolean shouldActionBeEnabled(AnActionEvent event) {
        return (event.getData(CommonDataKeys.VIRTUAL_FILE) != null);
    }

    public void consume(Integer integer) {
        lineNumber = integer;
    }
}