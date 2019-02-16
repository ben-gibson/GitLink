package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class BitbucketServerUrlFactory extends TemplatedUrlFactory
{

    public BitbucketServerUrlFactory(URLTemplateProcessor urlTemplateProcessor)
    {
        super(
            urlTemplateProcessor,
            "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at=refs/heads/{branch}#{line:start}-{line:end}",
            "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at={commit}#{line:start}-{line:end}",
            "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/commits/{commit}"
        );
    }


    public boolean supports(RemoteHost host)
    {
        return host.isBitbucketServer();
    }

}
