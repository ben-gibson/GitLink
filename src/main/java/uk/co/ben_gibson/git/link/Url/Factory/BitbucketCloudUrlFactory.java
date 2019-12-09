package uk.co.ben_gibson.git.link.Url.Factory;

import com.intellij.serviceContainer.NonInjectable;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class BitbucketCloudUrlFactory extends TemplatedUrlFactory
{
    public BitbucketCloudUrlFactory() {
        super(
            "{remote:url}/src/HEAD/{file:path}/{file:name}?at={branch}#lines-{line:start}:{line:end}",
            "{remote:url}/src/{commit}/{file:path}/{file:name}#lines-{line:start}:{line:end}",
            "{remote:url}/commits/{commit}"
        );
    }

    @NonInjectable
    public BitbucketCloudUrlFactory(@NotNull final URLTemplateProcessor processor) {
        super(
            processor,
            "{remote:url}/src/HEAD/{file:path}/{file:name}?at={branch}#lines-{line:start}:{line:end}",
            "{remote:url}/src/{commit}/{file:path}/{file:name}#lines-{line:start}:{line:end}",
            "{remote:url}/commits/{commit}"
        );
    }

    @Override
    public boolean supports(@NotNull final RemoteHost host) {
        return host.isBitbucketCloud();
    }
}
