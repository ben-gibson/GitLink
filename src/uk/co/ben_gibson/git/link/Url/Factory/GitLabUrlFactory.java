package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class GitLabUrlFactory extends TemplatedUrlFactory
{
    public GitLabUrlFactory(URLTemplateProcessor urlTemplateProcessor)
    {
        super(
            urlTemplateProcessor,
            "{remote:url}/blob/{branch}/{file:path}/{file:name}#L{line:start}-{line:end}",
            "{remote:url}/blob/{commit}/{file:path}/{file:name}#L{line:start}-{line:end}",
            "{remote:url}/commit/{commit}"
        );
    }

    public boolean supports(RemoteHost host)
    {
        return host.isGitLab();
    }
}
