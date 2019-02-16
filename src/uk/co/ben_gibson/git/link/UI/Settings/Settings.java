package uk.co.ben_gibson.git.link.UI.Settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.*;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import uk.co.ben_gibson.git.link.Plugin;
import uk.co.ben_gibson.git.link.Preferences;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifier;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings
{
    private JPanel rootPanel;
    private JComboBox hostSelect;
    private JTextField defaultBranchTextField;
    private JTextField                     customFileUrlOnBranchTemplateTextField;
    private JTextField                     customCommitUrlTemplateTextField;
    private JPanel                         customURLPanel;
    private JLabel                         customFileUrlOnBranchLabel;
    private JLabel                         customCommitUrlLabel;
    private JLabel                         projectSettingsLabel;
    private JLabel                         customUrlLabel;
    private JPanel                         urlModifierCheckBoxPanel;
    private HyperlinkLabel                 featureRequestLabel;
    private JLabel                         pluginDetailsLabel;
    private JTextField                     customFileUrlAtCommitTemplateTextField;
    private JLabel                         customFileUrlAtCommitLabel;
    private JTextField                     remoteNameTextField;
    private JCheckBox                      enabledCheckBox;
    private HyperlinkLabel                 customTemplateHelpLabel;
    private Preferences                    preferences;
    private Map<UrlModifier, JBCheckBox>   urlModifierCheckBoxes = new HashMap<>();

    public Settings(Preferences preferences, List<UrlModifier> urlModifiers, Plugin plugin)
    {
        this.preferences = preferences;

        $$$setupUI$$$();
        this.enabledCheckBox.setSelected(this.preferences.enabled);
        this.hostSelect.setModel(new EnumComboBoxModel<>(RemoteHost.class));
        this.defaultBranchTextField.setText(this.preferences.defaultBranchName);
        this.remoteNameTextField.setText(this.preferences.remoteName);
        this.customURLPanel.setVisible(this.preferences.getRemoteHost().isCustom());

        this.applyLabelHelpTextStlye(this.customFileUrlAtCommitLabel);
        this.applyLabelHelpTextStlye(this.customFileUrlOnBranchLabel);
        this.applyLabelHelpTextStlye(this.customCommitUrlLabel);
        this.applyLabelHeadingStlye(this.projectSettingsLabel);
        this.applyLabelHeadingStlye(this.customUrlLabel);

        this.hostSelect.addActionListener(e -> {
            RemoteHost host = (((RemoteHost) hostSelect.getSelectedItem()));
            Boolean isCustomHost = (host != null && host.isCustom());

            // TODO: Move the label to the custom URL panel so we only need to toggle one component.
            Settings.this.customURLPanel.setVisible(isCustomHost);
            this.customTemplateHelpLabel.setVisible(isCustomHost);
        });

        for (UrlModifier modifier : urlModifiers) {
            JBCheckBox checkBox = new JBCheckBox(modifier.name());
            this.urlModifierCheckBoxes.put(modifier, checkBox);
            this.urlModifierCheckBoxPanel.add(checkBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        }

        this.pluginDetailsLabel.setText(plugin.toString());
        this.applyLabelHelpTextStlye(this.pluginDetailsLabel);

        this.featureRequestLabel.setHtmlText("Submit feature requests and bug reports <a href=\"#\">here</a>.");
        this.featureRequestLabel.setHyperlinkTarget(plugin.issueTracker());

        this.customTemplateHelpLabel.setHtmlText("For more information on the custom host type visit the docs <a href=\"#\">here</a>.");
        this.customTemplateHelpLabel.setHyperlinkTarget(plugin.customHostDocs());
        this.customTemplateHelpLabel.setVisible(false);

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
            this.preferences.isEnabled() != this.enabledCheckBox.isSelected() ||
            !this.preferences.getRemoteHost().equals(this.hostSelect.getSelectedItem()) ||
            !this.preferences.remoteName.equals(this.remoteNameTextField.getText()) ||
            !this.preferences.defaultBranchName.equals(this.defaultBranchTextField.getText()) ||
            !this.preferences.customFileUrlAtCommitTemplate.equals(this.customFileUrlAtCommitTemplateTextField.getText()) ||
            !this.preferences.customFileUrlOnBranchTemplate.equals(this.customFileUrlOnBranchTemplateTextField.getText()) ||
            !this.preferences.customCommitUrlTemplate.equals(this.customCommitUrlTemplateTextField.getText());
    }

    public void apply() throws ConfigurationException
    {
        this.preferences.enabled = this.enabledCheckBox.isSelected();

        RemoteHost remoteHost = (RemoteHost) this.hostSelect.getSelectedItem();

        if (remoteHost.equals(RemoteHost.CUSTOM)) {

            if (this.customFileUrlOnBranchTemplateTextField.getText().isEmpty()) {
                this.applyErrorStyling(this.customFileUrlOnBranchTemplateTextField);
                throw new ConfigurationException("Invalid URL template provided for file at branch.");
            }

            if (this.customFileUrlAtCommitTemplateTextField.getText().isEmpty()) {
                this.applyErrorStyling(this.customFileUrlAtCommitTemplateTextField);
                throw new ConfigurationException("Invalid URL template provided for file at commit.");
            }


            if (this.customCommitUrlTemplateTextField.getText().isEmpty()) {
                this.applyErrorStyling(this.customCommitUrlTemplateTextField);
                throw new ConfigurationException("Invalid URL template provided for commit.");
            }

            this.clearErrorStyling(this.customFileUrlOnBranchTemplateTextField);
            this.clearErrorStyling(this.customFileUrlAtCommitTemplateTextField);
            this.clearErrorStyling(this.customCommitUrlTemplateTextField);

            this.preferences.customFileUrlOnBranchTemplate = this.customFileUrlOnBranchTemplateTextField.getText();
            this.preferences.customFileUrlAtCommitTemplate = this.customFileUrlAtCommitTemplateTextField.getText();
            this.preferences.customCommitUrlTemplate       = this.customCommitUrlTemplateTextField.getText();
        }

        if (this.defaultBranchTextField.getText().isEmpty()) {
            throw new ConfigurationException("Default branch is required");
        }

        if (this.remoteNameTextField.getText().isEmpty()) {
            throw new ConfigurationException("Remote name is required");
        }

        this.preferences.remoteName        = this.remoteNameTextField.getText();
        this.preferences.defaultBranchName = this.defaultBranchTextField.getText();
        this.preferences.remoteHostId      = remoteHost.name();

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
        this.enabledCheckBox.setSelected(this.preferences.isEnabled());
        this.remoteNameTextField.setText(this.preferences.remoteName);
        this.defaultBranchTextField.setText(this.preferences.defaultBranchName);
        this.hostSelect.setSelectedItem(this.preferences.getRemoteHost());
        this.customFileUrlAtCommitTemplateTextField.setText(this.preferences.customFileUrlAtCommitTemplate);
        this.customFileUrlOnBranchTemplateTextField.setText(this.preferences.customFileUrlOnBranchTemplate);
        this.customCommitUrlTemplateTextField.setText(this.preferences.customCommitUrlTemplate);

        this.clearErrorStyling(this.customFileUrlOnBranchTemplateTextField);
        this.clearErrorStyling(this.customFileUrlAtCommitTemplateTextField);
        this.clearErrorStyling(this.customCommitUrlTemplateTextField);

        for (Map.Entry<UrlModifier, JBCheckBox> entry : this.urlModifierCheckBoxes.entrySet()) {
            entry.getValue().setSelected(this.preferences.isModifierEnabled(entry.getKey()));
        }
    }

    private void clearErrorStyling(JTextField textField)
    {
        textField.setBorder(BorderFactory.createEmptyBorder());
    }

    private void applyErrorStyling(JTextField textField)
    {
        textField.setBorder(BorderFactory.createLineBorder(JBColor.RED, 1));
    }

    private void applyLabelHelpTextStlye(JLabel label)
    {
        label.setFont(new Font(null, Font.ITALIC, 11));
    }

    private void applyLabelHeadingStlye(JLabel label)
    {
        label.setFont(new Font(null, Font.PLAIN, 12));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Gray._200));
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
        rootPanel.setLayout(new FormLayout("fill:d:grow", "center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
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
        customURLPanel.setLayout(new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        customURLPanel.setVisible(true);
        rootPanel.add(customURLPanel, cc.xy(1, 13));
        final JLabel label2 = new JLabel();
        label2.setText("File on branch");
        customURLPanel.add(label2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        customFileUrlOnBranchTemplateTextField = new JTextField();
        customURLPanel.add(customFileUrlOnBranchTemplateTextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        customFileUrlOnBranchLabel = new JLabel();
        customFileUrlOnBranchLabel.setHorizontalAlignment(10);
        customFileUrlOnBranchLabel.setHorizontalTextPosition(11);
        customFileUrlOnBranchLabel.setText("e.g. https://example.com/ben-gibson/GitLink/blob/{branch}/{filePath}/{fileName}#{line} ");
        customFileUrlOnBranchLabel.setVerticalTextPosition(0);
        customFileUrlOnBranchLabel.setVisible(true);
        customURLPanel.add(customFileUrlOnBranchLabel, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Commit");
        customURLPanel.add(label3, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customCommitUrlTemplateTextField = new JTextField();
        customURLPanel.add(customCommitUrlTemplateTextField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        customCommitUrlLabel = new JLabel();
        customCommitUrlLabel.setText("e.g. https://example.com/ben-gibson/GitLink/commit/{commit}   ");
        customURLPanel.add(customCommitUrlLabel, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        customURLPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        customUrlLabel = new JLabel();
        customUrlLabel.setText("Custom URL");
        customURLPanel.add(customUrlLabel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("File at commit");
        customURLPanel.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customFileUrlAtCommitTemplateTextField = new JFormattedTextField();
        customURLPanel.add(customFileUrlAtCommitTemplateTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        customFileUrlAtCommitLabel = new JLabel();
        customFileUrlAtCommitLabel.setText("e.g. e.g. https://example.com/ben-gibson/GitLink/commit/{commit}/{filePath}/{fileName}#{line} ");
        customURLPanel.add(customFileUrlAtCommitLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel2, cc.xy(1, 5));
        final JLabel label5 = new JLabel();
        label5.setText("Default Branch");
        panel2.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        defaultBranchTextField = new JTextField();
        panel2.add(defaultBranchTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel3, cc.xy(1, 9));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        rootPanel.add(spacer4, cc.xy(1, 15, CellConstraints.DEFAULT, CellConstraints.FILL));
        projectSettingsLabel = new JLabel();
        projectSettingsLabel.setText("Project Settings");
        projectSettingsLabel.setVerticalAlignment(0);
        rootPanel.add(projectSettingsLabel, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));
        rootPanel.add(urlModifierCheckBoxPanel, cc.xy(1, 11));
        featureRequestLabel = new HyperlinkLabel();
        featureRequestLabel.setText("Label");
        rootPanel.add(featureRequestLabel, cc.xy(1, 19));
        pluginDetailsLabel = new JLabel();
        pluginDetailsLabel.setText("Label");
        rootPanel.add(pluginDetailsLabel, cc.xy(1, 17));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel4, cc.xy(1, 7));
        final JLabel label6 = new JLabel();
        label6.setText("Remote name");
        panel4.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        remoteNameTextField = new JTextField();
        panel4.add(remoteNameTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return rootPanel;
    }
}
