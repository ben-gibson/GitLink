package uk.co.ben_gibson.git.link.Url.Modifier.Exception;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import java.net.URL;

public class ModifierException extends GitLinkException
{
    private ModifierException(@NotNull final  String message)
    {
        super(message);
    }

    public static ModifierException invalidUrlAfterModification(@NotNull final URL url)
    {
        return new ModifierException(String.format("The url '%s' became invalid after modification", url));
    }
}
