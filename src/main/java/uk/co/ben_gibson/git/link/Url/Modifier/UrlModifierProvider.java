package uk.co.ben_gibson.git.link.Url.Modifier;

import java.util.Arrays;
import java.util.List;

public class UrlModifierProvider
{
    private List<UrlModifier> modifiers;

    public UrlModifierProvider()
    {
        this.modifiers = Arrays.asList(
             new HttpsUrlModifier()
        );
    }

    public List<UrlModifier> modifiers()
    {
        return this.modifiers;
    }
}
