package uk.co.ben_gibson.repositorymapper.Settings;

import com.intellij.openapi.project.Project;
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
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides the configuration support that is used in the IDE settings dialog.
 */
public class Configuration implements Configurable
{

    private static final String SETTINGS_DISPLAY_NAME = "Remote Repository Mappings";

    private static final String PATH_COLUMN    = "Path";
    private static final String PROJECT_COLUMN = "Project";
    private static final String REPO_COLUMN    = "Repo";

    private static final String LABEL_ADD    = "+";
    private static final String LABEL_REMOVE = "-";

    private static final String LABEL_COPY_TO_CLIPBOARD = "Copy to clipboard";
    private static final String LABEL_HOST              = "Remote repository provider";

    private static final String HEADER_OPTIONS  = "Options";
    private static final String HEADER_MAPPINGS = "Mappings";

    private JTable    table;
    private JBCheckBox copyToClipboardCheckBox;
    private JTextField hostTextField;

    private Boolean isDirty = false;

    private Settings settings;


    /**
     * Constructor.
     *
     * @param project  The project.
     */
    public Configuration(Project project)
    {
        this.settings = ServiceManager.getService(project, Settings.class);
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

        panel.add(this.getOptionsPanel());
        panel.add(this.getMappingsTablePanel());

        return panel;
    }


    /**
     * Get the mappings table panel.
     *
     * @return JPanel
     */
    private JPanel getMappingsTablePanel()
    {
        JPanel panel = new JPanel();

        panel.setBorder(IdeBorderFactory.createTitledBorder(HEADER_MAPPINGS));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        buttonsPanel.add(this.getAddButton());
        buttonsPanel.add(this.getRemoveButton());

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

        panel.setBorder(IdeBorderFactory.createTitledBorder(HEADER_OPTIONS));

        panel.add(this.getCopyToClipboardCheckbox());
        panel.add(new JBLabel(LABEL_HOST));
        panel.add(this.getHostTextField());

        panel.add(Box.createHorizontalGlue());

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
                    isDirty = true;
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
     * Lazy load the host text field.
     *
     * @return JTextField
     */
    private JTextField getHostTextField()
    {
        if (this.hostTextField == null) {
            this.hostTextField = new JTextField(30);
        }

        return this.hostTextField;
    }


    /**
     * Lazy load the copy to clipboard checkbox.
     *
     * @return JBChJBCheckBoxeckBox
     */
    private JBCheckBox getCopyToClipboardCheckbox()
    {
        if (this.copyToClipboardCheckBox == null) {
            this.copyToClipboardCheckBox = new JBCheckBox(LABEL_COPY_TO_CLIPBOARD, this.settings.shouldCopyToClipboard());
        }

        return this.copyToClipboardCheckBox;
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
     * Get the add mapping button.
     *
     * @return JButton
     */
    private JButton getAddButton()
    {

        JButton button = new JButton(LABEL_ADD);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getMappingsTableModel().addRow(new Object[]{"", "", ""});
            }
        });

        this.styleButton(button);

        return button;
    }


    /**
     * Get the remove mapping button.
     *
     * @return JButton
     */
    private JButton getRemoveButton()
    {

        JButton button = new JButton(LABEL_REMOVE);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[] rows = table.getSelectedRows();

                for(int i = 0; i <rows.length; i++){
                    getMappingsTableModel().removeRow(rows[i]-i);
                }
            }
        });

        this.styleButton(button);

        return button;
    }


    /**
     * {@inheritDoc}
     *
     * This determines if the 'apply' button should be disabled.
     */
    public boolean isModified()
    {
        return (
            this.isDirty ||
            this.getCopyToClipboardCheckbox().isSelected() != this.settings.shouldCopyToClipboard() ||
            this.getHostTextField().getText() != this.settings.getHost().toString()
        );

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
        this.settings.setCopyToClipboard(this.getCopyToClipboardCheckbox().isSelected());
        this.settings.setHost(new Host(this.getHostTextField().getText()));

        if (tableModel.getRowCount() > 0) {

            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {

                String directoryPath = (String) tableModel.getValueAt(i, 0);
                String project       = (String) tableModel.getValueAt(i, 1);
                String repository    = (String) tableModel.getValueAt(i, 2);

                if (StringUtils.isNotBlank(directoryPath) && StringUtils.isNotBlank(project) && StringUtils.isNotBlank(repository)) {
                    this.settings.getMappingList().add(new Mapping(directoryPath, project, repository));
                }
            }
        }

        this.isDirty = false;
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
     * {@inheritDoc}
     */
    public void reset()
    {
        this.removeAllRows();

        for(Mapping mapping : this.settings.getMappingList()){
            this.getMappingsTableModel().addRow(new Object[]{mapping.getBaseDirectoryPath(), mapping.getProject(), mapping.getRepository()});
        }

        if (this.settings.getHost() != null) {
            this.getHostTextField().setText(this.settings.getHost().toString());
        }

        this.getCopyToClipboardCheckbox().setSelected(this.settings.shouldCopyToClipboard());

        this.isDirty = false;
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
    public void disposeUIResources()
    {
        this.table = null;
        this.copyToClipboardCheckBox = null;
        this.hostTextField = null;
    }


    /**
     * {@inheritDoc}
     */
    public String getHelpTopic()
    {
        return null;
    }



    /**
     * {@inheritDoc}
     *
     * Returns the name shown in the settings panel.
     */
    public String getDisplayName()
    {
        return SETTINGS_DISPLAY_NAME; // TODO: It's been a ball ache trying to find out how to get this from plugin.xml?
    }
}
