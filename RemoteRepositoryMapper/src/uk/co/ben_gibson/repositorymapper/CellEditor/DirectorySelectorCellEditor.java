package uk.co.ben_gibson.repositorymapper.CellEditor;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import org.apache.commons.lang.StringUtils;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * Directory selector within a cell.
 */
public class DirectorySelectorCellEditor extends DefaultCellEditor implements TableCellEditor {

    private JButton button;
    private String directoryPath = "";


    /**
     * Constructor.
     */
    public DirectorySelectorCellEditor()
    {
        super(new JTextField());

        this.setClickCountToStart(2);

        button = new JButton();
        button.setBackground(JBColor.WHITE);
        button.setFont(button.getFont().deriveFont(Font.PLAIN));
        button.setBorder(null);
    }


    /**
     * {@inheritDoc}
     */
    public Object getCellEditorValue()
    {
        return this.directoryPath;
    }


    /**
     * {@inheritDoc}
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        this.directoryPath = value.toString();

        VirtualFile baseDir;

        if (StringUtils.isNotBlank(value.toString())) {
            baseDir = LocalFileSystem.getInstance().findFileByPath(value.toString());
        } else {
            // Can't seem to find a way to get the active project?
            baseDir = ProjectManager.getInstance().getOpenProjects()[0].getBaseDir();
        }

        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        VirtualFile file = FileChooser.chooseFile(descriptor, null, baseDir);

        if (file != null) {
            this.directoryPath = file.getCanonicalPath();
        }

        button.setText(this.directoryPath);

        return button;
    }
}
