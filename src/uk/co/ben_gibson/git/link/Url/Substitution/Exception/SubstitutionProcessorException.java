package uk.co.ben_gibson.git.link.Url.Substitution.Exception;

import uk.co.ben_gibson.git.link.Exception.GitLinkException;

public class SubstitutionProcessorException extends GitLinkException
{
    private SubstitutionProcessorException(String message)
    {
        super(message);
    }


    public static SubstitutionProcessorException cannotCreateUrlFromTemplate(String template)
    {
        return new SubstitutionProcessorException(
            String.format("Could not create a valid URL using the template (%s)", template)
        );
    }
}
