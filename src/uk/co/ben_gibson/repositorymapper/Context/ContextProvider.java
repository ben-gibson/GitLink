package uk.co.ben_gibson.repositorymapper.Context;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitLocalBranch;
import git4idea.GitUtil;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Context Provider.
 */
public class ContextProvider
{

    /**
     * Provides a context based on the current environment.
     *
     * @param project The active project.
     *
     * @return Context
     */
    @Nullable
    public Context getContext(@NotNull Project project) throws MalformedURLException, ContextProviderException {

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        if (editor == null) {
            return null;
        }

        VirtualFile file =  FileDocumentManager.getInstance().getFile(editor.getDocument());

        if (file == null) {
            return null;
        }

        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFile(file);

        if (repository == null) {
            return null;
        }

        URL remoteHost = this.getRemoteHostFromRepository(repository);

        GitLocalBranch branch = null;

        if (repository.getCurrentBranch() != null && repository.getCurrentBranch().findTrackedBranch(repository) != null) {
            branch = repository.getCurrentBranch();
        }

        String path = file.getPath().substring(repository.getRoot().getPath().length());
        String branchName = branch != null ? branch.getName() : null;

        Integer caretPosition = editor.getCaretModel().getLogicalPosition().line + 1;

        return new Context(remoteHost, path, branchName, caretPosition);
    }


    /**
     * Get a clean url from the repositories remote origin.
     *
     * @return URL
     */
    @NotNull
    private URL getRemoteHostFromRepository(@NotNull GitRepository repository) throws MalformedURLException, ContextProviderException
    {
        GitRemote origin = null;

        for (GitRemote remote : repository.getRemotes()) {
            if (remote.getName().equals("origin")) {
                origin = remote;
            }
        }

        if (origin == null) {
            throw ContextProviderException.originRemoteNotFound(repository);
        }

        if (origin.getFirstUrl() == null) {
            throw ContextProviderException.urlNotFoundForRemote(origin);
        }

        String url = StringUtil.trimEnd(origin.getFirstUrl(), ".git");

        url = url.replaceAll(":\\d{1,4}", ""); // remove port

        if (url.startsWith("http")) {
           return new URL(url);
        }

        url = StringUtil.replace(url, "git@", "");
        url = StringUtil.replace(url, "ssh://", "");

        url = "https://" + StringUtil.replace(url, ":", "/");

        return new URL(url);
    }
}
