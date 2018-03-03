package uk.co.ben_gibson.git.link.Url.Modifier;
import java.util.List;

public class UrlModifierProvider
{
    private List<UrlModifier> modifiers;


    public UrlModifierProvider(List<UrlModifier> modifiers)
    {
        this.modifiers = modifiers;
    }


    public List<UrlModifier> modifiers()
    {
        return this.modifiers;
    }
}
