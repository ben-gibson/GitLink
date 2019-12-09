package uk.co.ben_gibson.git.link.UI.Action.Annotation.Commit;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.annotate.FileAnnotation;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.GitLink;
import uk.co.ben_gibson.git.link.UI.Action.Annotation.AnnotationAction;
import uk.co.ben_gibson.git.link.UI.LineSelection;

public class ClipboardCommitAnnotationAction extends AnnotationAction {

    public ClipboardCommitAnnotationAction(@NotNull FileAnnotation annotation) {
        super(annotation);
    }

    @Override
    protected String displayName(@NotNull final RemoteHost remoteHost) {
        return String.format("Copy %s link", remoteHost.toString());
    }

    @Override
    protected void perform(
        @NotNull final Project project,
        @NotNull final Commit commit,
        @NotNull final VirtualFile file,
        @NotNull final LineSelection lineSelection
    ) {
        GitLink.getInstance(project).copyCommit(commit, file);
    }
}
