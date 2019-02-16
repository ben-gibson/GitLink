package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.UI.LineSelection;
import uk.co.ben_gibson.git.link.Url.Substitution.URLTemplateProcessor;

public class BitbucketCloudUrlFactory extends TemplatedUrlFactory
{

    public BitbucketCloudUrlFactory(URLTemplateProcessor urlTemplateProcessor)
    {
        super(
            urlTemplateProcessor,
            "{remote:url}/src/HEAD/{file:path}/{file:name}?at={branch}#lines-{line:start}:{line:end}",
            "{remote:url}/src/{commit}/{file:path}/{file:name}#lines-{line:start}:{line:end}",
            "{remote:url}/commits/{commit}"
        );
    }


    public boolean supports(RemoteHost host)
    {
        return host.isBitbucketCloud();
    }


    private String formatLineSelection(LineSelection lineSelection)
    {
        if (lineSelection.isMultiLineSelection()) {
            return String.format("lines-%d:%d", lineSelection.start(), lineSelection.end());
        }

        return String.format("lines-%d", lineSelection.start());
    }
}
