package uk.co.ben_gibson.repositorymapper.Host.Url.Factory;

import com.intellij.util.containers.hash.LinkedHashMap;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Host.Url.Exception.ProjectNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a URL in a format expected by GitBlit.
 */
public class GitBlit implements Factory {

    /**
     * {@inheritDoc}
     *
     * Mapping logic from https://github.com/markiewb/nb-git-open-in-external-repoviewer
     */
    @Override
    @NotNull
    public URL createUrl(@NotNull Context context, boolean forceSSL) throws URISyntaxException, UnsupportedEncodingException, MalformedURLException, RemoteNotFoundException, ProjectNotFoundException {

        String remoteUrl = context.getRepository().getOrigin().getFirstUrl();

        for (Map.Entry<String, String> urlPattern : this.getUrlPatternsForContext(context).entrySet()) {

            String pattern = urlPattern.getKey();
            String url     = urlPattern.getValue();

            Matcher matcher = Pattern.compile(pattern).matcher(remoteUrl);

            // if the remote URL doesn't match the pattern then move onto the next one.
            if (!matcher.find()) {
                continue;
            }

            // Get the placeholder values for the URL
            Map<String, String> placeholders = this.getPlaceholdersForContext(context);

            placeholders.put("<protocol>", (forceSSL) ? "https" : this.getGroupFromMatcher(matcher, "protocol"));
            placeholders.put("<server>", this.getGroupFromMatcher(matcher, "server"));
            placeholders.put("<repo>", this.getGroupFromMatcher(matcher, "repo"));
            placeholders.put("<repo\\|replaceSlashWithBang >", this.replaceSlashWithBang(this.getGroupFromMatcher(matcher, "repo")));

            // Replace the URL placeholders
            for (Map.Entry<String, String> placeHolder : placeholders.entrySet()) {
                url = url.replaceAll(placeHolder.getKey(), placeHolder.getValue());
            }

            return new URL(url);
        }

        throw new IllegalArgumentException(remoteUrl + " could not be parsed correctly");
    }

    /**
     * Get URL patterns relevant to the context.
     *
     * Note: Use LinkedHashMap because ordering the patterns have a priority
     *
     * @param context The context to get relevant URL patterns for.
     *
     * @return Map
     */
    @NotNull
    private Map<String, String> getUrlPatternsForContext(@NotNull Context context)
    {
        Map<String, String> patterns = new LinkedHashMap<>();

        if (context.getFilePathRelativeToRepository().isEmpty()) {
            patterns.put("(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git/r)/(?<repo>.+)\\.git", "<protocol>://<server>/git/commitdiff/<repo|replaceSlashWithBang >.git/<revision>");
            patterns.put("(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git|r)/(?<repo>.+)\\.git", "<protocol>://<server>/commitdiff/<repo|replaceSlashWithBang >.git/<revision>");
        } else {
            patterns.put("(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git/r)/(?<repo>.+)\\.git", "<protocol>://<server>/git/blob/<repo|replaceSlashWithBang >.git/<revision>/<fullfilepath|replaceSlashWithBang >#L<linenumber|1based>");
            patterns.put("(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git|r)/(?<repo>.+)\\.git", "<protocol>://<server>/blob/<repo|replaceSlashWithBang >.git/<revision>/<fullfilepath|replaceSlashWithBang >#L<linenumber|1based>");
        }

        return patterns;
    }

    /**
     * Get placeholders for a given context.
     *
     * @param context The context to get placeholders for.
     *
     * @return Map
     */
    @NotNull
    private Map<String, String> getPlaceholdersForContext(@NotNull Context context) throws RemoteNotFoundException
    {
        Map<String, String> placeholders = new HashMap<>();

        String revision = (context.getCommitHash() != null) ? context.getCommitHash() : context.getBranch();
        String line     = (context.getCaretLinePosition() == null) ? "1" : context.getCaretLinePosition().toString();

        placeholders.put("<revision>", revision);
        placeholders.put("<linenumber\\|1based>", line);

        String path = context.getFilePathRelativeToRepository();

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        placeholders.put("<fullfilepath>", path);
        placeholders.put("<fullfilepath\\|replaceSlashWithBang >", this.replaceSlashWithBang(path));

        return placeholders;
    }

    /**
     * Get a selected group from a matcher.
     *
     * @param matcher The matcher to get a group from.
     * @param name The group to get from the matcher.
     *
     * @return String
     */
    private String getGroupFromMatcher(Matcher matcher, String name)
    {
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
     *
     * @return String
     */
    @NotNull
    private String replaceSlashWithBang (@NotNull String string)
    {
        return string.replaceAll("/", "!");
    }
}
