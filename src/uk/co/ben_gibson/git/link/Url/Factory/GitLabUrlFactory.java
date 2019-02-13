package uk.co.ben_gibson.git.link.Url.Factory;

import uk.co.ben_gibson.git.link.Git.*;
import uk.co.ben_gibson.git.link.UI.LineSelection;

import java.net.URL;

public class GitLabUrlFactory extends GitHubUrlFactory
{
    public boolean supports(RemoteHost host)
    {
        return host.isGitLab();
    }

    protected String formatLineSelection(LineSelection lineSelection)
    {
        if (lineSelection.isMultiLineSelection()) {
            return String.format("L%d-%d", lineSelection.start(), lineSelection.end());
        }

        return String.format("L%d", lineSelection.start());
    }
}
