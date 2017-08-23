package uk.co.ben_gibson.open.in.git.host;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import com.intellij.openapi.options.ConfigurationException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration implements Configurable
{
    private static final String LABEL_HOSTS      = "Hosts";
    private static final String FORCE_SSL        = "Force SSL";
    private static final String EVENT_LOGGING    = "Enable verbose event logging";
    private static final String LABEL_EXTENSIONS = "Extensions";
    private static final String LABEL_OPTIONS    = "Options";

    private Settings settings;

    private JBCheckBox enableVerboseEventLogging;
    private JBCheckBox forceSSLCheckBox;
    private ComboBox hostsComboBox;
    private Container container;

    private Map<Extension, JBCheckBox> extensionCheckBoxes = new HashMap<>();

    /**
     * Needs cleaning up - dumping ground for preferences UI.
     *
     * Now idea how to inject the dependencies as this is wired up in the plugin.xml and auto-magically created :(
     */
    public Configuration(Project project, Container container)
    {
        this.container = container;
        this.settings = container.settings(project);

        this.enableVerboseEventLogging = new JBCheckBox(EVENT_LOGGING);
        this.forceSSLCheckBox          = new JBCheckBox(FORCE_SSL);
        this.hostsComboBox             = new ComboBox(new EnumComboBoxModel<>(RemoteHost.class), 200);

        for (Extension extension: this.container.registeredExtensions()) {
            extensionCheckBoxes.put(extension, new JBCheckBox(extension.displayName()));
        }
    }

    public JComponent createComponent()
    {
        this.reset();

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.hostsComboBox.setAlignmentX(0.0f);

        this.addToPanel(panel, new JBLabel(LABEL_HOSTS));
        this.addToPanel(panel, this.hostsComboBox);
        this.addSpacing(panel);
        this.addToPanel(panel, new JBLabel(LABEL_EXTENSIONS));

        for (JCheckBox extensionCheckBox : this.extensionCheckBoxes.values()) {
            this.addToPanel(panel, extensionCheckBox);
        }

        this.addSpacing(panel);

        this.addToPanel(panel, new JBLabel(LABEL_OPTIONS));
        this.addToPanel(panel, this.enableVerboseEventLogging);
        this.addToPanel(panel, this.forceSSLCheckBox);

        return panel;
    }

    public boolean isModified()
    {
        for (Map.Entry<Extension, JBCheckBox> entry : this.extensionCheckBoxes.entrySet()) {
            Extension extension = entry.getKey();
            JBCheckBox checkBox = entry.getValue();

            if (checkBox.isSelected() != this.settings.isExtensionEnabled(extension)) {
                return true;
            }
        }

        return
            this.forceSSLCheckBox.isSelected() != this.settings.getForceSSL() ||
            this.enableVerboseEventLogging.isSelected() != this.settings.getEnableVerboseEventLog() ||
            this.hostsComboBox.getSelectedItem() != this.settings.getRemoteHost();
    }

    public void apply() throws ConfigurationException
    {
        this.settings.setForceSSL(this.forceSSLCheckBox.isSelected());
        this.settings.setEnableVerboseEventLog(this.enableVerboseEventLogging.isSelected());
        this.settings.setRemoteHost((RemoteHost) this.hostsComboBox.getSelectedItem());

        List<Extension> enabledExtensions = new ArrayList<>();

        for (Map.Entry<Extension, JBCheckBox> entry : this.extensionCheckBoxes.entrySet()) {
            Extension extension = entry.getKey();
            JBCheckBox checkBox = entry.getValue();

            if (checkBox.isSelected()) {
                enabledExtensions.add(extension);
            }
        }

        this.settings.setEnabledExtensions(enabledExtensions);
    }

    public void reset()
    {
        this.enableVerboseEventLogging.setSelected(this.settings.getEnableVerboseEventLog());
        this.forceSSLCheckBox.setSelected(this.settings.getForceSSL());
        this.hostsComboBox.setSelectedItem(this.settings.getRemoteHost());

        for (Map.Entry<Extension, JBCheckBox> entry : this.extensionCheckBoxes.entrySet()) {

            Extension extension = entry.getKey();
            JBCheckBox checkBox = entry.getValue();

            checkBox.setSelected(this.settings.isExtensionEnabled(extension));
        }
    }

    public void disposeUIResources()
    {
        this.enableVerboseEventLogging = null;
        this.forceSSLCheckBox    = null;
        this.hostsComboBox       = null;
        this.extensionCheckBoxes = null;
    }

    public String getHelpTopic()
    {
        return "Foo bar";
    }

    public String getDisplayName()
    {
        return this.container.plugin().displayName();
    }

    private void addSpacing(JPanel panel)
    {
        JPanel spacing = new JPanel();

        spacing.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        this.addToPanel(panel, spacing);
    }

    private void addToPanel(JPanel panel, JComponent component)
    {
        component.setMaximumSize(component.getPreferredSize());

        panel.add(component);
    }
}
