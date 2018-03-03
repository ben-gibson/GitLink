package uk.co.ben_gibson.git.link.UI.Settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.git.link.Container;
import uk.co.ben_gibson.git.link.Plugin;
import uk.co.ben_gibson.git.link.Preferences;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifierProvider;
import javax.swing.*;

public class ConfigurableSettings implements Configurable
{
    private final Project project;
    private uk.co.ben_gibson.git.link.UI.Settings.Settings ui;


    public ConfigurableSettings(Project project)
    {
        this.project = project;
    }


    public JComponent createComponent()
    {
        this.ui = new uk.co.ben_gibson.git.link.UI.Settings.Settings(
            Preferences.getInstance(project),
            Container.getInstance().urlModifierProvider().modifiers(),
            Plugin.createDefault()
        );

        return ui.getRootPanel();
    }


    @Override
    public void disposeUIResources()
    {
        this.ui = null;
    }


    public String getHelpTopic()
    {
        return Plugin.createDefault().displayName();
    }


    public String getDisplayName()
    {
        return Plugin.createDefault().displayName();
    }


    public boolean isModified()
    {
        return this.ui.isModified();
    }


    public void apply() throws ConfigurationException
    {
        this.ui.apply();
    }


    public void reset()
    {
        this.ui.reset();
    }
}
