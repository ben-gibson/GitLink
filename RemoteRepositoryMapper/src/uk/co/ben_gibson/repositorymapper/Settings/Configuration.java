package uk.co.ben_gibson.repositorymapper.Settings;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import uk.co.ben_gibson.repositorymapper.CellEditor.DirectorySelectorCellEditor;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.apache.commons.lang.StringUtils;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.RepositoryProvider;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides the configuration support that is used in the IDE settings dialog.
 */
public class Configuration implements Configurable
{

    private static final String PATH_COLUMN    = "Path";
    private static final String PROJECT_COLUMN = "Project";
    private static final String REPO_COLUMN    = "Repo";

    private static final String LABEL_ADD    = "+";
    private static final String LABEL_REMOVE = "-";

    private static final String LABEL_COPY_TO_CLIPBOARD = "Copy to clipboard";
    private static final String LABEL_HOST              = "Host";
    private static final String LABEL_PROVIDERS         = "Providers";

    private static final String HEADER_OPTIONS  = "Options";
    private static final String HEADER_MAPPINGS = "Mappings";

    private JTable    table;
    private JBCheckBox copyToClipboardCheckBox;
    private JTextField hostTextField;
    private ComboBox providerComboBox;

    private JButton addButton;
    private JButton removeButton;

    private Boolean isMappingsTableDirty = false;

    private Settings settings;


    /**
     * Constructor.
     *
     * @param project  The project.
     */
    public Configuration(Project project)
    {
        this.settings = ServiceManager.getService(project, Settings.class);

        this.copyToClipboardCheckBox = new JBCheckBox(LABEL_COPY_TO_CLIPBOARD);
        this.hostTextField           = new JTextField(30);
        this.providerComboBox        = new ComboBox(new EnumComboBoxModel<>(RepositoryProvider.class), 200);

        this.addButton    = new JButton(LABEL_ADD);
        this.removeButton = new JButton(LABEL_REMOVE);
    }


    /**
     * Creates the panel component that is rendered in the setting dialog.
     *
     * @return JPanel
     */
    public JComponent createComponent()
    {
        this.reset();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        this.registerActionListeners();

        panel.add(this.getOptionsPanel());
        panel.add(this.getMappingsTablePanel());

        return panel;
    }


    /**
     * Create action listeners.
     */
    private void registerActionListeners()
    {
        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getMappingsTableModel().addRow(new Object[]{"", "", ""});
            }
        });

        this.removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[] rows = table.getSelectedRows();

                for(int i = 0; i <rows.length; i++){
                    getMappingsTableModel().removeRow(rows[i]-i);
                }
            }
        });

        this.providerComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    hostTextField.setText("");
                }
            }
        });

    }


    /**
     * Get the mappings table panel.
     *
     * @return JPanel
     */
    private JPanel getMappingsTablePanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        panel.setBorder(IdeBorderFactory.createTitledBorder(HEADER_MAPPINGS));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        this.styleButton(this.addButton);
        buttonsPanel.add(this.addButton);

        this.styleButton(this.removeButton);
        buttonsPanel.add(this.removeButton);

        panel.add(new JBScrollPane(this.getMappingsTable()));
        panel.add(buttonsPanel);

        return panel;
    }


    /**
     * Get the options panel.
     *
     * @return JPanel
     */
    private JPanel getOptionsPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 0));

        panel.setBorder(IdeBorderFactory.createTitledBorder(HEADER_OPTIONS));

        panel.add(new JBLabel(LABEL_HOST));
        panel.add(this.hostTextField);

        panel.add(new JBLabel(LABEL_PROVIDERS));
        panel.add(this.providerComboBox);

        panel.add(this.copyToClipboardCheckBox);

        return panel;
    }


    /**
     * Lazy load the table that is used to manage the different remote repository mappings.
     *
     * @return JTable
     */
    private JTable getMappingsTable()
    {
        if (this.table == null) {

            DefaultTableModel tableModel = new DefaultTableModel();

            tableModel.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    isMappingsTableDirty = true;
                }
            });

            tableModel.addColumn(PATH_COLUMN);
            tableModel.addColumn(PROJECT_COLUMN);
            tableModel.addColumn(REPO_COLUMN);

            JTable table = new JBTable(tableModel);

            table.getColumn(PATH_COLUMN).setCellEditor(new DirectorySelectorCellEditor());

            this.table = table;
        }

        return this.table;
    }


    /**
     * Style a JButton.
     *
     * @param button The button to style.
     */
    private void styleButton(JButton button)
    {
        button.setBorder(BorderFactory.createEmptyBorder());
    }


    /**
     * {@inheritDoc}
     *
     * This determines if the 'apply' button should be disabled.
     */
    public boolean isModified()
    {
        return this.isMappingsTableDirty ||
            !Comparing.equal(this.copyToClipboardCheckBox.isSelected(), this.settings.copyToClipboard()) ||
            !Comparing.equal(this.hostTextField.getText(), this.settings.getHost()) ||
            this.providerComboBox.getSelectedItem() != this.settings.getRepositoryProvider();
    }


    /**
     * {@inheritDoc}
     *
     * Saves the changes.
     */
    public void apply() throws ConfigurationException
    {
        DefaultTableModel tableModel = this.getMappingsTableModel();

        this.settings.getMappingList().clear();

        this.settings.setCopyToClipboard(this.copyToClipboardCheckBox.isSelected());
        this.settings.setRepositoryProvider((RepositoryProvider) this.providerComboBox.getSelectedItem());

        try {
            URL host = new URL(this.hostTextField.getText().trim());
            this.settings.setHost(host.toString());
        } catch (MalformedURLException e) {
            throw new ConfigurationException("Invalid host");
        }

        if (tableModel.getRowCount() > 0) {

            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {

                String directoryPath = (String) tableModel.getValueAt(i, 0);
                String project       = (String) tableModel.getValueAt(i, 1);
                String repository    = (String) tableModel.getValueAt(i, 2);

                if (!StringUtils.isNotBlank(directoryPath) || !StringUtils.isNotBlank(project) || !StringUtils.isNotBlank(repository)) {
                    throw new ConfigurationException("Missing fields for mapping at row " + (i + 1));
                }

                this.settings.getMappingList().add(new Mapping(directoryPath, project, repository));
            }
        }

        this.isMappingsTableDirty = false;
    }


    /**
     * Get the model that drives the table.
     *
     * @return DefaultTableModel
     */
    private DefaultTableModel getMappingsTableModel()
    {
        return (DefaultTableModel) this.getMappingsTable().getModel();
    }


    /**
     * Removes all rows from the table.
     */
    private void removeAllRows()
    {
        DefaultTableModel tableModel = this.getMappingsTableModel();

        if (tableModel.getRowCount() > 0) {
            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                tableModel.removeRow(i);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void reset()
    {
        this.removeAllRows();

        for(Mapping mapping : this.settings.getMappingList()){
            this.getMappingsTableModel().addRow(new Object[]{mapping.getBaseDirectoryPath(), mapping.getProject(), mapping.getRepository()});
        }

        this.hostTextField.setText(this.settings.getHost());
        this.copyToClipboardCheckBox.setSelected(this.settings.copyToClipboard());
        this.providerComboBox.setSelectedItem(this.settings.getRepositoryProvider());

        this.isMappingsTableDirty = false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeUIResources()
    {
        this.table = null;
        this.copyToClipboardCheckBox = null;
        this.hostTextField = null;
        this.providerComboBox = null;
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
        return "Remote Repository Mappings";
    }
}
