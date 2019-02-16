package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class GogsUrlFactory extends TemplatedUrlFactory
{
    public GogsUrlFactory(URLTemplateProcessor urlTemplateProcessor)
    {
        super(
            urlTemplateProcessor,
            "{remote:url}/src/{branch}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/src/{commit}/{file:path}/{file:name}#L{line:start}-L{line:end}",
            "{remote:url}/commit/{commit}"
        );
    }


    public boolean supports(RemoteHost host)
    {
        return host.isGogs() || host.isGitea();
    }
}
