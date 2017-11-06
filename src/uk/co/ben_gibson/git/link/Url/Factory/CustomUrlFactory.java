package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;
import java.net.MalformedURLException;
import java.net.URL;

public class CustomUrlFactory extends AbstractUrlFactory
{
    private String fileUrlTemplate;
    private String commitUrlTemplate;

    public CustomUrlFactory(String fileUrlTemplate, String commitUrlTemplate)
    {
        this.fileUrlTemplate   = fileUrlTemplate;
        this.commitUrlTemplate = commitUrlTemplate;
    }

    public URL createUrl(CommitDescription description) throws UrlFactoryException, RemoteException
    {
        String url = this.commitUrlTemplate.replace("{commit}", description.commitHash());

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw UrlFactoryException.cannotCreateUrl(String.format("Custom url '%s' is invalid.", url));
        }
    }

    public URL createUrl(FileDescription description) throws UrlFactoryException, RemoteException
    {
        String url = this.fileUrlTemplate.replace("{branch}", description.branch().toString());
        url = url.replace("{filePath}", this.cleanPath(description.file().path()));
        url = url.replace("{fileName}", description.file().name());
        url = url.replace("{line}", description.hasLineNumber() ? description.lineNumber().toString() : "");

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw UrlFactoryException.cannotCreateUrl(String.format("Custom url '%s' is invalid.", url));
        }
    }

    public boolean supports(RemoteHost host)
    {
        return host.custom();
    }
}
