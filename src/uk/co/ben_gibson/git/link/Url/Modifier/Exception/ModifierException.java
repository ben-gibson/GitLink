package uk.co.ben_gibson.git.link.Url.Modifier.Exception;

import uk.co.ben_gibson.git.link.Exception.Codes;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import java.net.URL;

public class ModifierException extends GitLinkException
{
    private ModifierException(String message, int code)
    {
        super(message, code);
    }


    public static ModifierException invalidUrlAfterModification(URL url)
    {
        return new ModifierException(
            String.format("The url '%s' became invalid after modification", url),
            Codes.URL_INVALID_AFTER_MODIFICATION
        );
    }
}
