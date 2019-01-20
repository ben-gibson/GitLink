package uk.co.ben_gibson.git.link.Url.Factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;

import java.net.MalformedURLException;
import java.net.URL;

public class CustomUrlFactory extends AbstractUrlFactory
{
    private String fileOnBranchUrlTemplate;
    private String fileAtCommitUrlTemplate;
    private String commitUrlTemplate;


    public CustomUrlFactory(String fileOnBranchUrlTemplate, String fileAtCommitUrlTemplate, String commitUrlTemplate)
    {
        this.fileOnBranchUrlTemplate = fileOnBranchUrlTemplate;
        this.fileAtCommitUrlTemplate = fileAtCommitUrlTemplate;
        this.commitUrlTemplate = commitUrlTemplate;
    }


    public URL createUrlToCommit(@NotNull Remote remote, @NotNull Commit commit) throws UrlFactoryException
    {
        String url = this.commitUrlTemplate.replace("{commit}", commit.hash());

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw UrlFactoryException.cannotCreateUrl(String.format("Custom url '%s' is invalid.", url));
        }
    }


    public URL createUrlToFileOnBranch(
        @NotNull Remote remote,
        @NotNull File file,
        @NotNull Branch branch,
        @Nullable LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException
    {
        String template = this.fileOnBranchUrlTemplate.replace("{branch}", branch.toString());

        template = template.replace("{filePath}", this.cleanPath(file.directoryPath()));
        template = template.replace("{fileName}", file.name());
        template = template.replace("{line}", (lineSelection != null) ? Integer.toString(lineSelection.start()) : "");

        return this.createUrlFromTemplate(template);
    }


    public URL createUrlToFileAtCommit(
        @NotNull Remote remote,
        @NotNull File file,
        @NotNull Commit commit,
        @Nullable LineSelection lineSelection
    ) throws UrlFactoryException, RemoteException
    {
        String template = this.fileAtCommitUrlTemplate.replace("{commit}", commit.hash());

        template = template.replace("{filePath}", this.cleanPath(file.directoryPath()));
        template = template.replace("{fileName}", file.name());
        template = template.replace("{line}", (lineSelection != null) ? Integer.toString(lineSelection.start()) : "");

        return this.createUrlFromTemplate(template);
    }


    @NotNull
    private URL createUrlFromTemplate(String template) throws UrlFactoryException
    {
        try {
            URL url = new URL(template);

            String path = url.getPath().replace("//", "/");

            if (url.getQuery() != null && !url.getQuery().isEmpty()) {
                path = path.concat("?" + url.getQuery());
            }

            if (url.getRef() != null && !url.getRef().isEmpty()) {
                path = path.concat("#" + url.getRef());
            }

            return new URL(url.getProtocol(), url.getHost(), url.getPort(), path);

        } catch (MalformedURLException e) {
            throw UrlFactoryException.cannotCreateUrl(String.format("Custom url '%s' is invalid.", template));
        }
    }


    public boolean supports(RemoteHost host)
    {
        return host.isCustom();
    }
}
