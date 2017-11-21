package uk.co.ben_gibson.git.link.Url.Factory;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Exception.RemoteException;
import uk.co.ben_gibson.git.link.Git.Remote;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Factory.Description.CommitDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Description.FileDescription;
import uk.co.ben_gibson.git.link.Url.Factory.Exception.UrlFactoryException;

public class GitBlitUrlFactory extends AbstractUrlFactory {
    @Override
    public URL createUrl(CommitDescription description) throws RemoteException, UrlFactoryException {
        Remote remote = description.remote();

        String remoteUrl = description.remote().url().getPath();

        Map<String, String> patterns = new LinkedHashMap<>();

        patterns.put("(?<context>/.+?)?/(git/r)/(?<repo>.+)", "<context>/git/commitdiff/<repo>.git/<revision>");
        patterns.put("(?<context>/.+?)?/(git|r)/(?<repo>.+)", "<context>/commitdiff/<repo>.git/<revision>");

        for (Map.Entry<String, String> urlPattern : patterns.entrySet()) {
            String pattern = urlPattern.getKey();
            String url = urlPattern.getValue();

            Matcher matcher = Pattern.compile(pattern).matcher(remoteUrl);
            if (matcher.matches()) {

                // Get the placeholder values for the URL
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("<context>", getContext(matcher));
                placeholders.put("<repo>", this.replaceSlashWithBang(this.getGroupFromMatcher(matcher, "repo")));
                placeholders.put("<revision>", description.commitHash());

                // Replace the URL placeholders
                for (Map.Entry<String, String> placeHolder : placeholders.entrySet()) {
                    url = url.replaceAll(placeHolder.getKey(), placeHolder.getValue());
                }

                return this.buildURL(remote, url, null, null);
            }
        }

        throw UrlFactoryException.cannotCreateUrl(String.format("Repository URL %s is not supported", remoteUrl));
    }

    @Override
    public URL createUrl(FileDescription description) throws RemoteException, UrlFactoryException {
        Remote remote = description.remote();

        String remoteUrl = description.remote().url().getPath();

        Map<String, String> patterns = new LinkedHashMap<>();

        patterns.put("(?<context>/.+?)?/(git/r)/(?<repo>.+)", "<context>/git/blob/<repo>.git/<branch>/<fullfilepath>");
        patterns.put("(?<context>/.+?)?/(git|r)/(?<repo>.+)", "<context>/blob/<repo>.git/<branch>/<fullfilepath>");

        for (Map.Entry<String, String> urlPattern : patterns.entrySet()) {

            String pattern = urlPattern.getKey();
            String url = urlPattern.getValue();

            Matcher matcher = Pattern.compile(pattern).matcher(remoteUrl);

            if (matcher.matches()) {

                // Get the placeholder values for the URL
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("<context>", getContext(matcher));
                placeholders.put("<repo>", this.replaceSlashWithBang(this.getGroupFromMatcher(matcher, "repo")));
                placeholders.put("<branch>", this.replaceSlashWithBang(description.branch().toString()));
                placeholders.put("<fullfilepath>", this.replaceSlashWithBang(description.file().pathWithName()));

                // Replace the URL placeholders
                for (Map.Entry<String, String> placeHolder : placeholders.entrySet()) {
                    url = url.replaceAll(placeHolder.getKey(), placeHolder.getValue());
                }

                String fragment = null;
                if (description.hasLineNumber()) {
                    fragment = String.format("L%d", description.lineNumber());
                }

                return this.buildURL(remote, url, null, fragment);
            }
        }

        throw UrlFactoryException.cannotCreateUrl(String.format("Repository URL %s is not supported", remoteUrl));
    }

    @Override
    public boolean supports(RemoteHost host) {
        return host.gitBlit();
    }

    private String getContext(Matcher matcher) {
        String context = this.getGroupFromMatcher(matcher, "context");
        if (context != null) {
            return context;
        }
        return "";
    }

    /**
     * Get a selected group from a matcher.
     *
     * @param matcher The matcher to get a group from.
     * @param name    The group to get from the matcher.
     * @return String
     */
    private String getGroupFromMatcher(Matcher matcher, String name) {
        try {
            return matcher.group(name);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return "";
        }
    }

    /**
     * Replace forward slash character with bang.
     *
     * @param string The string to replace forward slash characters.
     * @return String
     */
    @NotNull
    private String replaceSlashWithBang(@NotNull String string) {
        return string.replaceAll("/", "!");
    }
}
