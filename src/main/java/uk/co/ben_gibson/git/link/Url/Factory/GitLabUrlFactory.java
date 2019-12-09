package uk.co.ben_gibson.git.link.Url.Factory;

import com.intellij.serviceContainer.NonInjectable;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class GitLabUrlFactory extends TemplatedUrlFactory
{
    public GitLabUrlFactory() {
        super(
            "{remote:url}/blob/{branch}/{file:path}/{file:name}#L{line:start}-{line:end}",
            "{remote:url}/blob/{commit}/{file:path}/{file:name}#L{line:start}-{line:end}",
            "{remote:url}/commit/{commit}"
        );
    }

    @NonInjectable
    public GitLabUrlFactory(@NotNull final URLTemplateProcessor processor) {
        super(
            processor,
            "{remote:url}/blob/{branch}/{file:path}/{file:name}#L{line:start}-{line:end}",
            "{remote:url}/blob/{commit}/{file:path}/{file:name}#L{line:start}-{line:end}",
            "{remote:url}/commit/{commit}"
        );
    }

    @Override
    public boolean supports(@NotNull final RemoteHost host) {
        return host.isGitLab();
    }
}
