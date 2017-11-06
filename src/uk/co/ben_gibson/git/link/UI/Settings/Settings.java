package uk.co.ben_gibson.git.link.UI.Settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.Gray;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Plugin;
import uk.co.ben_gibson.git.link.Preferences;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifier;
import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings
{
    private JPanel rootPanel;
    private JComboBox hostSelect;
    private JTextField defaultBranchTextField;
    private JTextField customFileUrlTemplateTextField;
    private JTextField customCommitUrlTemplateTextField;
    private JPanel customURLPanel;
    private JCheckBox verboseLoggingCheckBox;
    private JLabel customFileUrlLabel;
    private JLabel customCommitUrlLabel;
    private JLabel projectSettingsLabel;
    private JLabel customUrlLabel;
    private JPanel urlModifierCheckBoxPanel;
    private JLabel featureRequestLabel;
    private JLabel pluginDetailsLabel;
    private Preferences preferences;
    private Map<UrlModifier, JBCheckBox> urlModifierCheckBoxes = new HashMap<>();

    public Settings(Preferences preferences, List<UrlModifier> urlModifiers, Plugin plugin)
    {
        this.preferences = preferences;

        $$$setupUI$$$();
        this.hostSelect.setModel(new EnumComboBoxModel<>(RemoteHost.class));
        this.defaultBranchTextField.setText(this.preferences.getDefaultBranch().toString());
        this.customURLPanel.setVisible(this.preferences.getRemoteHost().custom());

        this.applyLabelHelpTextStlye(this.customFileUrlLabel);
        this.applyLabelHelpTextStlye(this.customCommitUrlLabel);
        this.applyLabelHeadingStlye(this.projectSettingsLabel);
        this.applyLabelHeadingStlye(this.customUrlLabel);

        this.hostSelect.addActionListener(e -> {
            RemoteHost host = (((RemoteHost) hostSelect.getSelectedItem()));
            Settings.this.customURLPanel.setVisible((host != null && host.custom()));
        });

        for (UrlModifier modifier : urlModifiers) {
            JBCheckBox checkBox = new JBCheckBox(modifier.name());
            this.urlModifierCheckBoxes.put(modifier, checkBox);
            this.urlModifierCheckBoxPanel.add(checkBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        }

        this.pluginDetailsLabel.setText(plugin.toString());
        this.featureRequestLabel.setText(String.format("Submit feature requests and bug reports to %s", plugin.issueTracker()));
        this.applyLabelHelpTextStlye(this.pluginDetailsLabel);
        this.applyLabelHelpTextStlye(this.featureRequestLabel);

    }

    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    public boolean isModified()
    {
        for (Map.Entry<UrlModifier, JBCheckBox> entry : this.urlModifierCheckBoxes.entrySet()) {
            if (entry.getValue().isSelected() != this.preferences.isModifierEnabled(entry.getKey())) {
                return true;
            }
        }

        return
            this.verboseLoggingCheckBox.isSelected() != this.preferences.getEnableVerboseEventLog() ||
            this.hostSelect.getSelectedItem() != this.preferences.getRemoteHost() ||
            !this.preferences.getDefaultBranch().equals(new Branch(this.defaultBranchTextField.getText())) ||
            !this.preferences.getCustomFileUrlTemplate().equals(this.customFileUrlTemplateTextField.getText()) ||
            !this.preferences.getCustomCommitUrlTemplate().equals(this.customCommitUrlTemplateTextField.getText());
    }

    public void apply() throws ConfigurationException
    {
        RemoteHost remoteHost = (RemoteHost) this.hostSelect.getSelectedItem();

        if (remoteHost != null && remoteHost.custom()) {

            try {
               URL url = new URL(this.customFileUrlTemplateTextField.getText());
               this.preferences.setCustomFileUrlTemplate(url.toString());
            } catch (MalformedURLException exception) {
                throw new ConfigurationException("Invalid URL provided for the custom file URL");
            }

            try {
                URL url = new URL(this.customCommitUrlTemplateTextField.getText());
                this.preferences.setCustomCommitUrlTemplate(url.toString());
            } catch (MalformedURLException exception) {
                throw new ConfigurationException("Invalid URL provided for the custom commit URL");
            }
        }

        if (this.defaultBranchTextField.getText().isEmpty()) {
            throw new ConfigurationException("Default branch is required");
        }

        this.preferences.setDefaultBranch(new Branch(this.defaultBranchTextField.getText()));
        this.preferences.setEnableVerboseEventLog(this.verboseLoggingCheckBox.isSelected());
        this.preferences.setRemoteHost(remoteHost);

        for (Map.Entry<UrlModifier, JBCheckBox> entry : this.urlModifierCheckBoxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                this.preferences.enableModifier(entry.getKey());
            } else {
                this.preferences.disableModifier(entry.getKey());
            }
        }
    }

    public void reset()
    {
        this.defaultBranchTextField.setText(this.preferences.getDefaultBranch().toString());
        this.verboseLoggingCheckBox.setSelected(this.preferences.getEnableVerboseEventLog());
        this.hostSelect.setSelectedItem(this.preferences.getRemoteHost());
        this.customFileUrlTemplateTextField.setText(this.preferences.getCustomFileUrlTemplate());
        this.customCommitUrlTemplateTextField.setText(this.preferences.getCustomCommitUrlTemplate());

        for (Map.Entry<UrlModifier, JBCheckBox> entry : this.urlModifierCheckBoxes.entrySet()) {
            entry.getValue().setSelected(this.preferences.isModifierEnabled(entry.getKey()));
        }
    }

    private void applyLabelHeadingStlye(JLabel label)
    {
        label.setFont(new Font(null, Font.PLAIN, 12));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Gray._200));
    }

    private void applyLabelHelpTextStlye(JLabel label)
    {
        label.setFont(new Font(null, Font.ITALIC, 11));
    }

    private void createUIComponents()
    {
        this.urlModifierCheckBoxPanel = new JPanel();
        this.urlModifierCheckBoxPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new FormLayout("fill:d:grow", "center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow"));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        CellConstraints cc = new CellConstraints();
        rootPanel.add(panel1, cc.xy(1, 3));
        final JLabel label1 = new JLabel();
        label1.setText("Hosts");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        hostSelect = new JComboBox();
        panel1.add(hostSelect, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customURLPanel = new JPanel();
        customURLPanel.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        customURLPanel.setVisible(true);
        rootPanel.add(customURLPanel, cc.xy(1, 11));
        final JLabel label2 = new JLabel();
        label2.setText("File");
        customURLPanel.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        customFileUrlTemplateTextField = new JTextField();
        customURLPanel.add(customFileUrlTemplateTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        customFileUrlLabel = new JLabel();
        customFileUrlLabel.setHorizontalAlignment(10);
        customFileUrlLabel.setHorizontalTextPosition(11);
        customFileUrlLabel.setText("e.g. https://example.com/{repository}/blob/{branch}#{line}  ");
        customFileUrlLabel.setVerticalTextPosition(0);
        customFileUrlLabel.setVisible(true);
        customURLPanel.add(customFileUrlLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Commit");
        customURLPanel.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customCommitUrlTemplateTextField = new JTextField();
        customURLPanel.add(customCommitUrlTemplateTextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        customCommitUrlLabel = new JLabel();
        customCommitUrlLabel.setText("e.g. https://example.com/{repository}/{commit}#{line}   ");
        customURLPanel.add(customCommitUrlLabel, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        customURLPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        customUrlLabel = new JLabel();
        customUrlLabel.setText("Custom URL");
        customURLPanel.add(customUrlLabel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel2, cc.xy(1, 5));
        final JLabel label4 = new JLabel();
        label4.setText("Default Branch");
        panel2.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        defaultBranchTextField = new JTextField();
        panel2.add(defaultBranchTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel3, cc.xy(1, 7));
        verboseLoggingCheckBox = new JCheckBox();
        verboseLoggingCheckBox.setText("Enable verbose logging");
        panel3.add(verboseLoggingCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        rootPanel.add(spacer4, cc.xy(1, 13, CellConstraints.DEFAULT, CellConstraints.FILL));
        projectSettingsLabel = new JLabel();
        projectSettingsLabel.setText("Project Settings");
        projectSettingsLabel.setVerticalAlignment(0);
        rootPanel.add(projectSettingsLabel, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));
        rootPanel.add(urlModifierCheckBoxPanel, cc.xy(1, 9));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return rootPanel;
    }
}
