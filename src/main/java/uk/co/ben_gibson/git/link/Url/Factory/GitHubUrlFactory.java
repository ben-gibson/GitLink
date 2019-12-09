package uk.co.ben_gibson.git.link.Url.Factory;

import com.intellij.serviceContainer.NonInjectable;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class GitHubUrlFactory extends TemplatedUrlFactory
{
    public GitHubUrlFactory() {
        super(
            "{remote:url}/blob/{branch}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/blob/{commit}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/commit/{commit}"
        );
    }

    @NonInjectable
    public GitHubUrlFactory(@NotNull final URLTemplateProcessor processor) {
        super(
            processor,
            "{remote:url}/blob/{branch}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/blob/{commit}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/commit/{commit}"
        );
    }

    @Override
    public boolean supports(@NotNull final RemoteHost host) {
        return host.isGitHub();
    }
}
