package uk.co.ben_gibson.git.link.UI.Settings;

import com.intellij.openapi.components.ServiceManager;
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
    private UrlModifierProvider urlModifierProvider;


    public ConfigurableSettings(Project project, UrlModifierProvider urlModifierProvider)
    {
        this.urlModifierProvider = urlModifierProvider;
        this.project             = project;
    }


    public JComponent createComponent()
    {
        this.ui = new uk.co.ben_gibson.git.link.UI.Settings.Settings(
            Preferences.getInstance(project),
            urlModifierProvider.modifiers(),
            ServiceManager.getService(Plugin.class)
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
        return ServiceManager.getService(Plugin.class).displayName();
    }


    public String getDisplayName()
    {
        return ServiceManager.getService(Plugin.class).displayName();
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
