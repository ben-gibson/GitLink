package uk.co.ben_gibson.repositorymapper.Settings;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import uk.co.ben_gibson.repositorymapper.Host.Host;
import javax.swing.*;

/**
 * Configuration used in the settings panel.
 */
public class Configuration implements Configurable
{

    private static final String LABEL_COPY_TO_CLIPBOARD = "Copy link to clipboard";
    private static final String FORCE_SSL               = "Force SSL if the HTTP protocol is not used in origin";
    private static final String LABEL_HOSTS             = "Hosts";

    private JBCheckBox copyToClipboardCheckBox;
    private JBCheckBox forceSSLCheckBox;
    private ComboBox hostsComboBox;

    private Settings settings;

    /**
     * Constructor.
     *
     * @param project The project.
     */
    public Configuration(Project project)
    {
        this.settings = ServiceManager.getService(project, Settings.class);

        this.forceSSLCheckBox        = new JBCheckBox(FORCE_SSL);
        this.copyToClipboardCheckBox = new JBCheckBox(LABEL_COPY_TO_CLIPBOARD);
        this.hostsComboBox           = new ComboBox(new EnumComboBoxModel<>(Host.class), 200);
    }

    /**
     * Creates the panel component that is rendered in the setting dialog.
     *
     * @return JPanel
     */
    @Override
    public JComponent createComponent()
    {
        this.reset();

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JBLabel label = new JBLabel(LABEL_HOSTS);
        label.setMaximumSize(label.getPreferredSize());

        this.hostsComboBox.setMaximumSize(this.hostsComboBox.getPreferredSize());
        this.hostsComboBox.setAlignmentX(0.0f);

        this.copyToClipboardCheckBox.setMaximumSize(this.copyToClipboardCheckBox.getPreferredSize());

        this.forceSSLCheckBox.setMaximumSize(this.forceSSLCheckBox.getPreferredSize());

        panel.add(label);
        panel.add(this.hostsComboBox);

        JPanel spacing = new JPanel();
        spacing.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        spacing.setMaximumSize(spacing.getPreferredSize());

        panel.add(spacing);

        panel.add(this.copyToClipboardCheckBox);

        panel.add(this.forceSSLCheckBox);

        return panel;
    }

    /**
     * {@inheritDoc}
     *
     * Determines if the 'apply' button should be disabled.
     */
    @Override
    public boolean isModified()
    {
        return !Comparing.equal(this.forceSSLCheckBox.isSelected(), this.settings.getForceSSL()) ||
            !Comparing.equal(this.copyToClipboardCheckBox.isSelected(), this.settings.getCopyToClipboard()) ||
            this.hostsComboBox.getSelectedItem() != this.settings.getHost();
    }

    /**
     * {@inheritDoc}
     *
     * Saves the changes.
     */
    @Override
    public void apply() throws ConfigurationException
    {
        this.settings.setForceSSL(this.forceSSLCheckBox.isSelected());
        this.settings.setCopyToClipboard(this.copyToClipboardCheckBox.isSelected());
        this.settings.setHost((Host) this.hostsComboBox.getSelectedItem());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset()
    {
        this.forceSSLCheckBox.setSelected(this.settings.getForceSSL());
        this.copyToClipboardCheckBox.setSelected(this.settings.getCopyToClipboard());
        this.hostsComboBox.setSelectedItem(this.settings.getHost());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeUIResources()
    {
        this.forceSSLCheckBox = null;
        this.copyToClipboardCheckBox = null;
        this.hostsComboBox = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpTopic()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName()
    {
        return "Remote Repository Mapper";
    }
}
