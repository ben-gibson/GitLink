package uk.co.ben_gibson.repositorymapper.Settings;

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
 *
 * This has been thrown together as I don't really know any Java!.
 */
public class Configuration implements Configurable
{

    final String SETTINGS_DISPLAY_NAME = "Remote repository mappings";

    final String PATH_COLUMN    = "Path";
    final String PROJECT_COLUMN = "Project";
    final String REPO_COLUMN    = "Repo";

    private JTable table;
    private Settings settings = ServiceManager.getService(Settings.class);
    private Boolean isDirty = false;


    /**
     * Creates the panel component that is rendered in the setting dialog.
     *
     * @return JPanel
     */
    public JComponent createComponent()
    {
        this.table = this.getTable();

        this.reset();

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JBScrollPane(this.table));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        buttonsPanel.add(this.getAddButton());
        buttonsPanel.add(this.getRemoveButton());

        panel.add(buttonsPanel);

        return panel;
    }


    /**
     * Get the table that is used to manage the different remote repository mappings.
     *
     * @return JTable
     */
    private JTable getTable()
    {

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

        return table;
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

        JButton button = new JButton("+");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getTableModel().addRow(new Object[]{"", "", ""});
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

        JButton button = new JButton("-");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[] rows = table.getSelectedRows();

                for(int i = 0; i <rows.length; i++){
                    getTableModel().removeRow(rows[i]-i);
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
        return this.isDirty;
    }


    /**
     * {@inheritDoc}
     *
     * Saves the changes.
     */
    public void apply() throws ConfigurationException
    {
        DefaultTableModel tableModel = this.getTableModel();

        this.settings.getMappingList().clear();

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
    private DefaultTableModel getTableModel()
    {
        return (DefaultTableModel) this.table.getModel();
    }


    /**
     * {@inheritDoc}
     */
    public void reset()
    {
        this.removeAllRows();

        for(Mapping mapping : this.settings.getMappingList()){
            this.getTableModel().addRow(new Object[]{mapping.getBaseDirectoryPath(), mapping.getProject(), mapping.getRepository()});
        }

        this.isDirty = false;
    }


    /**
     * Removes all rows from the table.
     */
    private void removeAllRows()
    {
        DefaultTableModel tableModel = this.getTableModel();

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
        this.removeAllRows();
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
