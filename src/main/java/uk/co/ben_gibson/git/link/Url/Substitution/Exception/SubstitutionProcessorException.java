package uk.co.ben_gibson.git.link.Url.Substitution.Exception;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;

public class SubstitutionProcessorException extends GitLinkException
{
    private SubstitutionProcessorException(@NotNull final String message)
    {
        super(message);
    }

    public static SubstitutionProcessorException cannotCreateUrlFromTemplate(@NotNull final String template)
    {
        return new SubstitutionProcessorException(
            String.format("Could not create a valid URL using the template (%s)", template)
        );
    }
}
