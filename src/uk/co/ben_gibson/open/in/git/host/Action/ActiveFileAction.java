package uk.co.ben_gibson.open.in.git.host.Action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import uk.co.ben_gibson.open.in.git.host.Container;

/**
 * An action that, when triggered, builds a remote host url from the active file and runs it though the registered extensions.
 */
public class ActiveFileAction extends AnAction
{
    private Container container;

    @Override
    public void actionPerformed(AnActionEvent event)
    {
        Container container = ServiceManager.getService(Container.class);

        container.getLogger().notice("Active file action triggered!");

        Project project  = event.getProject();
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);

        if (project == null || file == null) {
            return;
        }

        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFile(file);

        if (repository == null) {
            container.getLogger().error("Git repository not found, make sure version control root has not been registered in Preferences → Version Control");
            return;
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        Integer caretPosition = (editor != null) ? editor.getCaretModel().getLogicalPosition().line + 1 : null;
    }

    @Override
    public void update(AnActionEvent event)
    {
        super.update(event);

        event.getPresentation().setEnabled(this.shouldActionBeEnabled(event));

        if (event.getPresentation().isEnabled() && event.getProject() != null) {
            event.getPresentation().setIcon(this.getContainer().getRemoteHost().getIcon());
        }
    }

    /**
     * Should the action be enabled.
     *
     * @param event The action event.
     */
    private boolean shouldActionBeEnabled(AnActionEvent event)
    {
        return ((event.getProject() != null) && (event.getData(CommonDataKeys.VIRTUAL_FILE) != null));
    }

    /**
     * Get the plugin container.
     */
    private Container getContainer()
    {
        if (this.container == null) {
            this.container = new Container();
        }

        return this.container;
    }
}
