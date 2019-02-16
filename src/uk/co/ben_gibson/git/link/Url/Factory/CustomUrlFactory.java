package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class CustomUrlFactory extends TemplatedUrlFactory
{
    public CustomUrlFactory(
        URLTemplateProcessor urlTemplateProcessor,
        String fileAtBranchUrlTemplate,
        String fileAtCommitUrlTemplate,
        String commitUrlTemplate
    ) {
        super(urlTemplateProcessor, fileAtBranchUrlTemplate, fileAtCommitUrlTemplate, commitUrlTemplate);
    }

    public boolean supports(RemoteHost host)
    {
        return host.isCustom();
    }
}
