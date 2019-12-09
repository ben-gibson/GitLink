package uk.co.ben_gibson.git.link.Url.Factory;

import com.intellij.openapi.project.Project;
import com.intellij.serviceContainer.NonInjectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Preferences;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import uk.co.ben_gibson.git.link.Url.Substitution.Exception.SubstitutionProcessorException;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

import java.net.URL;

public class CustomUrlFactory implements UrlFactory
{
    private final URLTemplateProcessor urlTemplateProcessor;
    private final Preferences preferences;

    public CustomUrlFactory(@NotNull final Project project) {
        this.preferences = Preferences.getInstance(project);
        this.urlTemplateProcessor = URLTemplateProcessor.getInstance();
    }

    @NonInjectable
    public CustomUrlFactory(@NotNull final URLTemplateProcessor processor, @NotNull final Preferences preferences) {
        this.urlTemplateProcessor = processor;
        this.preferences = preferences;
    }

    @Override
    public URL createUrlToCommit(@NotNull final Remote remote, @NotNull final Commit commit) throws UrlFactoryException, RemoteException {
        try {
            return urlTemplateProcessor.process(preferences.customCommitUrlTemplate, remote, commit, null, null);
        } catch (SubstitutionProcessorException e) {
            throw UrlFactoryException.cannotCreateUrl(e.getMessage());
        }
    }

    @Override
    public URL createUrlToFileOnBranch(
        @NotNull final Remote remote,
        @NotNull final File file,
        @NotNull final Branch branch,
        @Nullable final LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException {
        try {
            return urlTemplateProcessor.process(preferences.customFileUrlOnBranchTemplate, remote, branch, file, lineSelection);
        } catch (SubstitutionProcessorException e) {
            throw UrlFactoryException.cannotCreateUrl(e.getMessage());
        }
    }

    @Override
    public URL createUrlToFileAtCommit(
        @NotNull final Remote remote,
        @NotNull final File file,
        @NotNull final Commit commit,
        @Nullable final LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException {
        try {
            return urlTemplateProcessor.process(preferences.customFileUrlAtCommitTemplate, remote, commit, file, lineSelection);
        } catch (SubstitutionProcessorException e) {
            throw UrlFactoryException.cannotCreateUrl(e.getMessage());
        }
    }

    @Override
    public boolean supports(@NotNull final RemoteHost host) {
        return host.isCustom();
    }
}
