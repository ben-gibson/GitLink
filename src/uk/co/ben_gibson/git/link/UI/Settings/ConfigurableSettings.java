package uk.co.ben_gibson.git.link.UI.Settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.git.link.Container;
import javax.swing.*;

public class ConfigurableSettings implements Configurable
{
    private final Container container;
    private final Project project;
    private uk.co.ben_gibson.git.link.UI.Settings.Settings ui;

    public ConfigurableSettings(Project project, Container container)
    {
        this.container = container;
        this.project   = project;
    }

    public JComponent createComponent()
    {
        this.ui = new uk.co.ben_gibson.git.link.UI.Settings.Settings(
            this.container.preferences(this.project),
            this.container.urlModifiers(),
            this.container.plugin()
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
        return this.container.plugin().displayName();
    }

    public String getDisplayName()
    {
        return this.container.plugin().displayName();
    }

    public boolean isModified()
    {
        return this.ui.isModified();
    }

    public void apply() throws ConfigurationException
    {
        this.ui.apply();

        // We need to flush the container when the plugins configuration has changed as many project level services
        // are constructed with values derived from the settings.
        this.container.flushProjectCache(this.project);
    }

    public void reset()
    {
        this.ui.reset();
    }
}
