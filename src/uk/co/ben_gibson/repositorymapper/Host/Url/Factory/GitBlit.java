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
     */
    @Override
    @NotNull
    public URL createUrl(@NotNull Context context, boolean forceSSL) throws URISyntaxException, UnsupportedEncodingException, MalformedURLException, RemoteNotFoundException, ProjectNotFoundException {

        String remoteUrl = context.getRepository().getOrigin().getFirstUrl();

        /**
         * Taken from https://github.com/markiewb/nb-git-open-in-external-repoviewer
         * <pre>
         gitblit.source.url.0=(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git/r)/(?<repo>.+)\.git
         gitblit.source.url.1=(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git|r)/(?<repo>.+)\.git
         gitblit.target.show_commitdiff.url.0=<protocol>://<server>/git/commitdiff/<repo|replaceSlashWithBang >.git/<revision>
         gitblit.target.show_commitdiff.url.1=<protocol>://<server>/commitdiff/<repo|replaceSlashWithBang >.git/<revision>
         gitblit.target.show_file.url.0=<protocol>://<server>/git/blob/<repo|replaceSlashWithBang >.git/<revision>/<fullfilepath|replaceSlashWithBang >#L<linenumber|1based>
         gitblit.target.show_file.url.1=<protocol>://<server>/blob/<repo|replaceSlashWithBang >.git/<revision>/<fullfilepath|replaceSlashWithBang >#L<linenumber|1based>
         * </pre>
         */


        //Note: Use LinkedHashMap because ordering the patterns have a priority
        Map<String, String> commitDiffPatterns = new LinkedHashMap<>();
        commitDiffPatterns.put("(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git/r)/(?<repo>.+)\\.git", "<protocol>://<server>/git/commitdiff/<repo|replaceSlashWithBang >.git/<revision>");
        commitDiffPatterns.put("(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git|r)/(?<repo>.+)\\.git", "<protocol>://<server>/commitdiff/<repo|replaceSlashWithBang >.git/<revision>");

        Map<String, String> fileViewPatterns = new LinkedHashMap<>();
        fileViewPatterns.put("(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git/r)/(?<repo>.+)\\.git", "<protocol>://<server>/git/blob/<repo|replaceSlashWithBang >.git/<revision>/<fullfilepath|replaceSlashWithBang >#L<linenumber|1based>");
        fileViewPatterns.put("(?<protocol>http|https)://(?<username>.+?@)?(?<server>.+?)/(git|r)/(?<repo>.+)\\.git", "<protocol>://<server>/blob/<repo|replaceSlashWithBang >.git/<revision>/<fullfilepath|replaceSlashWithBang >#L<linenumber|1based>");

        Map<String, String> patternToUse = (context.getFilePathRelativeToRepository().isEmpty()) ? commitDiffPatterns : fileViewPatterns;

        for (Map.Entry<String, String> patternEntry : patternToUse.entrySet()) {

            String pattern = patternEntry.getKey();
            Map<String, String> map = new URIPlaceHolderResolver().resolve(pattern, remoteUrl, forceSSL);
            if (map != null) {
                if (context.getCommitHash() != null) {
                    map.put("<revision>", context.getCommitHash());
                } else {
                    if (context.getBranch() != null) {
                        map.put("<revision>", context.getBranch());
                    }
                }
                if (null != context.getCaretLinePosition()) {
                    map.put("<linenumber\\|1based>", String.valueOf(context.getCaretLinePosition()));
                } else {
                    map.put("<linenumber\\|0based>", "0");
                    map.put("<linenumber\\|1based>", "1");
                }
                if (null != context.getFilePathRelativeToRepository()) {
                    String path = context.getFilePathRelativeToRepository();
                    if (path.startsWith("/")) {
                        path = path.substring(1);
                    }
                    map.put("<fullfilepath>", path);
                    map.put("<fullfilepath\\|replaceSlashWithBang >", replaceSlashWithBang (path));
                }

                String targetURL = patternEntry.getValue();
                for (Map.Entry<String, String> placeHolder : map.entrySet()) {
                    targetURL = targetURL.replaceAll(placeHolder.getKey(), placeHolder.getValue());
                }
                return new URL(targetURL);
            }
        }
        throw new IllegalArgumentException(remoteUrl + " could not be parsed correctly");
    }


    public class URIPlaceHolderResolver {


        public Map<String, String> resolve(String pattern, String URIFull, boolean forceSSL) {
            Matcher matcher = Pattern.compile(pattern).matcher(URIFull);

            if (matcher.find()) {

                Map<String, String> result = new HashMap<>();
                if (forceSSL) {
                    result.put("<protocol>", "https");
                } else {
                    result.put("<protocol>", extractFromURI(matcher, "protocol"));
                }
                result.put("<server>", extractFromURI(matcher, "server"));
                result.put("<repo>", extractFromURI(matcher, "repo"));
                result.put("<repo\\|replaceSlashWithBang >", replaceSlashWithBang (extractFromURI(matcher, "repo")));
                return result;
            } else {
                return null;
            }
        }


        private String extractFromURI(Matcher matcher, String name) {
            try {
                return matcher.group(name);
            } catch (IllegalArgumentException | IllegalStateException e) {
                return "";
            }
        }

    }

    private String replaceSlashWithBang (String repo) {
        return repo.replaceAll("/", "!");
    }
}
