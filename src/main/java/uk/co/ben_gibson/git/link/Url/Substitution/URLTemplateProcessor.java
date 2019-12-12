package uk.co.ben_gibson.git.link.Url.Substitution;

import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.File;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Substitution.Exception.SubstitutionProcessorException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLTemplateProcessor
{
    public static URLTemplateProcessor getInstance() {
        return ServiceManager.getService(URLTemplateProcessor.class);
    }

    public URL process(
        @NotNull final String template,
        @NotNull final Remote remote,
        @NotNull final Commit commit,
        @Nullable final File file,
        @Nullable final LineSelection lineSelection
    ) throws RemoteException, SubstitutionProcessorException {
        return process(template, remote, commit, null, file, lineSelection);
    }

    public URL process(
        @NotNull final String template,
        @NotNull final Remote remote,
        @NotNull final Branch branch,
        @NotNull final File file,
        @Nullable final LineSelection lineSelection
        ) throws RemoteException, SubstitutionProcessorException {
        return process(template, remote, null, branch, file, lineSelection);
    }

    private URL process(
        @NotNull final String template,
        @NotNull final Remote remote,
        @Nullable final Commit commit,
        @Nullable final Branch branch,
        @Nullable final File file,
        @Nullable final LineSelection lineSelection
    ) throws RemoteException, SubstitutionProcessorException {
        try {

            String processedTemplate = this.processRemote(template, remote);

            processedTemplate = this.processCommit(processedTemplate, commit);
            processedTemplate = this.processBranch(processedTemplate, branch);
            processedTemplate = this.processFile(processedTemplate, file);
            processedTemplate = this.processLineSelection(processedTemplate, lineSelection);

            return buildUrl(processedTemplate);
        } catch (MalformedURLException | URISyntaxException e) {
            throw SubstitutionProcessorException.cannotCreateUrlFromTemplate(template);
        }
    }

    private String processFile(@NotNull String template, @Nullable final File file) {
        String fileName = "";
        String filePath = "";

        if (file != null) {
            fileName = file.name();
            filePath = file.directoryPath();
        }

        template = template.replace("{file:name}", fileName);
        template = template.replace("{file:path}", filePath);

        // B/C
        template = template.replace("{fileName}", fileName);
        template = template.replace("{filePath}", filePath);

        return template;
    }

    private String processBranch(@NotNull final String template, @Nullable final Branch branch) {
        return template.replace("{branch}", (branch != null) ? branch.toString() : "");
    }

    private String processRemote(@NotNull String template, @NotNull final Remote remote) throws RemoteException {
        URL remoteURL = remote.url();

        template = template.replace("{remote:url:host}", remoteURL.getProtocol() + "://" + remoteURL.getHost());
        template = template.replace("{remote:url}", remoteURL.toString());
        template = template.replace("{remote:url:path}", remoteURL.getPath());

        // Split the remote path into parts on the / character.
        List<String> pathParts = new ArrayList<>(Arrays.asList(remoteURL.getPath().split("/")));
        pathParts.removeAll(Arrays.asList("", null));

        Pattern remotePathPattern = Pattern.compile("\\{remote:url:path:(\\d)\\}");
        Matcher remotePathMatcher = remotePathPattern.matcher(template);

        StringBuffer sb = new StringBuffer();

        // Find any matches of {remote:url:path:n} in the URL template, replacing it with the specified path part or an
        // empty string if it does not exist.
        while (remotePathMatcher.find()) {

            String pathPart = "";

            int position = Integer.parseInt(remotePathMatcher.group(1));

            if (pathParts.size() >= (position + 1)) {
                pathPart = pathParts.get(position);
            }

            remotePathMatcher.appendReplacement(sb, pathPart);
        }

        remotePathMatcher.appendTail(sb);

        return sb.toString();
    }

    private String processCommit(@NotNull String template, @Nullable final Commit commit) {
        String commitHash      = "";
        String commitHashShort = "";

        if (commit != null) {
            commitHash      = commit.hash();
            commitHashShort = commit.shortHash();
        }

        template = template.replace("{commit}", commitHash);
        template = template.replace("{commit:short}", commitHashShort);

        return template;
    }

    private String processLineSelection(@NotNull String template, @Nullable LineSelection lineSelection) {
        // It might be nicer to remove these substitutions from the template by replacing them with an empty string when
        // no line selection has been made however, this is a little more complicated for example, in GitHub line
        // selection is represented using the URL fragment in the format #L1-L20. Removing the line substitutions with
        // empty strings in this case would leave the fragment in an incomplete format i.e. #L-L
        if (lineSelection == null) {
            lineSelection = new LineSelection(0, 0);
        }

        template = template.replace("{line:start}", Integer.toString(lineSelection.start()));
        template = template.replace("{line:end}", Integer.toString(lineSelection.end()));

        // B/C
        template = template.replace("{line}", Integer.toString(lineSelection.start()));

        return template;
    }

    private URL buildUrl(@NotNull final String processedTemplate) throws MalformedURLException, URISyntaxException {
        URL url = new URL(processedTemplate);

        URI uri = new URI(
            url.getProtocol(),
            null,
            url.getHost(),
            url.getPort(),
            url.getPath().replaceAll("/{2,}", "/"),
            url.getQuery(),
            url.getRef()
        );

        return new URL(uri.toASCIIString());
    }
}
