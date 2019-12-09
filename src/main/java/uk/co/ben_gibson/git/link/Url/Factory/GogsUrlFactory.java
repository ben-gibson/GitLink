package uk.co.ben_gibson.git.link.Url.Factory;

import com.intellij.serviceContainer.NonInjectable;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class GogsUrlFactory extends TemplatedUrlFactory
{
    public GogsUrlFactory() {
        super(
            "{remote:url}/src/{branch}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/src/{commit}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/commit/{commit}"
        );
    }

    @NonInjectable
    public GogsUrlFactory(@NotNull final URLTemplateProcessor processor) {
        super(
            processor,
            "{remote:url}/src/{branch}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/src/{commit}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/commit/{commit}"
        );
    }

    @Override
    public boolean supports(@NotNull final RemoteHost host) {
        return host.isGogs() || host.isGitea();
    }
}
