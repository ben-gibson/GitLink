package uk.co.ben_gibson.git.link.UI.Settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
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
        ui = new uk.co.ben_gibson.git.link.UI.Settings.Settings(
            Preferences.getInstance(project),
            ApplicationManager.getApplication().getService(UrlModifierProvider.class).modifiers(),
            ApplicationManager.getApplication().getService(Plugin.class)
        );

        return ui.getRootPanel();
    }


    @Override
    public void disposeUIResources()
    {
        ui = null;
    }


    public String getHelpTopic()
    {
        return ApplicationManager.getApplication().getService(Plugin.class).displayName();
    }


    public String getDisplayName()
    {
        return ApplicationManager.getApplication().getService(Plugin.class).displayName();
    }


    public boolean isModified()
    {
        return ui.isModified();
    }


    public void apply() throws ConfigurationException
    {
        ui.apply();
    }


    public void reset()
    {
        ui.reset();
    }
}
