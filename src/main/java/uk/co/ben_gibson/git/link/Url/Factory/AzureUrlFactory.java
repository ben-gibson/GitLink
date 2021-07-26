package uk.co.ben_gibson.git.link.Url.Factory;

import com.intellij.serviceContainer.NonInjectable;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class AzureUrlFactory extends TemplatedUrlFactory
{
    public AzureUrlFactory() {
        super(
            "{remote:url}?version=GB{branch}&path=/{file:path}{file:name}&line={line:start}&lineEnd={line:end}&lineStartColumn=1&lineEndColumn=1",
            "{remote:url}?version=GC{commit}&path=/{file:path}{file:name}&line={line:start}&lineEnd={line:end}&lineStartColumn=1&lineEndColumn=1",
            "{remote:url}/commit/{commit}"
        );
    }

    @NonInjectable
    public AzureUrlFactory(@NotNull final URLTemplateProcessor processor) {
        super(
            processor,
                "{remote:url}?version=GB{branch}&path=/{file:path}{file:name}&line={line:start}&lineEnd={line:end}&lineStartColumn=1&lineEndColumn=1",
                "{remote:url}?version=GC{commit}&path=/{file:path}{file:name}&line={line:start}&lineEnd={line:end}&lineStartColumn=1&lineEndColumn=1",
                "{remote:url}/commit/{commit}"
        );
    }

    @Override
    public boolean supports(@NotNull final RemoteHost host) {
        return host.isAzure();
    }
}
