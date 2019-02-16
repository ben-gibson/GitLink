package uk.co.ben_gibson.git.link.Url.Factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import uk.co.ben_gibson.git.link.Url.Substitution.Exception.SubstitutionProcessorException;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

import java.net.URL;

public abstract class TemplatedUrlFactory implements UrlFactory
{
    private URLTemplateProcessor urlTemplateProcessor;
    private String fileAtBranchUrlTemplate;
    private String fileAtCommitUrlTemplate;
    private String commitUrlTemplate;


    public TemplatedUrlFactory(
        @NotNull URLTemplateProcessor urlTemplateProcessor,
        @NotNull String fileAtBranchUrlTemplate,
        @NotNull String fileAtCommitUrlTemplate,
        @NotNull String commitUrlTemplate
    ) {
        this.urlTemplateProcessor    = urlTemplateProcessor;
        this.fileAtBranchUrlTemplate = fileAtBranchUrlTemplate;
        this.fileAtCommitUrlTemplate = fileAtCommitUrlTemplate;
        this.commitUrlTemplate       = commitUrlTemplate;
    }


    public URL createUrlToCommit(@NotNull Remote remote, @NotNull Commit commit) throws UrlFactoryException, RemoteException
    {
        try {
            return this.urlTemplateProcessor.process(this.commitUrlTemplate, remote, commit, null, null);
        } catch (SubstitutionProcessorException e) {
            throw UrlFactoryException.cannotCreateUrl(e.getMessage());
        }
    }


    public URL createUrlToFileOnBranch(
        @NotNull Remote remote,
        @NotNull File file,
        @NotNull Branch branch,
        @Nullable LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException {
        try {
            return this.urlTemplateProcessor.process(this.fileAtBranchUrlTemplate, remote, branch, file, lineSelection);
        } catch (SubstitutionProcessorException e) {
            throw UrlFactoryException.cannotCreateUrl(e.getMessage());
        }
    }


    public URL createUrlToFileAtCommit(
        @NotNull Remote remote,
        @NotNull File file,
        @NotNull Commit commit,
        @Nullable LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException {
        try {
            return this.urlTemplateProcessor.process(this.fileAtCommitUrlTemplate, remote, commit, file, lineSelection);
        } catch (SubstitutionProcessorException e) {
            throw UrlFactoryException.cannotCreateUrl(e.getMessage());
        }
    }
}
