package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ApplicationSettings.CustomHostSettings
import javax.swing.ListSelectionModel.SINGLE_SELECTION

class ApplicationSettingsConfigurable : BoundConfigurable("Foo Bar", "Test") {
    private var settings = service<ApplicationSettings>()
    private var customHosts = settings.customHosts.toMutableList()

    private val customHostsTable = TableView(createCustomHostModel()).apply {
        setShowColumns(true)
        setSelectionMode(SINGLE_SELECTION)
        emptyText.text = "No custom hosts"
    }

    private val customHostTableContainer = ToolbarDecorator.createDecorator(customHostsTable)
        .setAddAction { addCustomHost() }
        .setEditAction { editCustomHost() }
        .setRemoveAction { removeCustomHost() }
        .createPanel()

    override fun createPanel() = panel {
        row {
            component(customHostTableContainer).constraints(CCFlags.grow)
        }
        row {
            browserLink("Report a bug", "https://github.com/ben-gibson/GitLink/issues")
        }
    }

    private fun createCustomHostModel(): ListTableModel<CustomHostSettings> = ListTableModel(
        arrayOf(
            createCustomHostColumn("Host Name") { customHost -> customHost?.displayName },
            createCustomHostColumn("Base URL") { customHost -> customHost?.baseUrl },
        ),
        customHosts
    )

    private fun createCustomHostColumn(name: String, formatter: (CustomHostSettings?) -> String?) : ColumnInfo<CustomHostSettings, String> {
        return object : ColumnInfo<CustomHostSettings, String>(name) {
            override fun valueOf(item: CustomHostSettings?): String? {
                return formatter(item)
            }
        }
    }

    private fun addCustomHost() {
        val dialog = CustomHostDialog(CustomHostSettings())

        if (dialog.showAndGet()) {
            customHosts.add(dialog.customHost)
            customHostsTable.tableViewModel.fireTableDataChanged()
        }
    }

    private fun removeCustomHost() {
        val row = customHostsTable.selectedObject ?: return

        customHosts.remove(row)
        customHostsTable.tableViewModel.fireTableDataChanged()
    }

    private fun editCustomHost() {
        val row = customHostsTable.selectedObject ?: return

        val dialog = CustomHostDialog(row.copy())

        if (dialog.showAndGet()) {
            customHosts[customHostsTable.selectedRow] = dialog.customHost
            customHostsTable.tableViewModel.fireTableDataChanged()
        }
    }

    override fun reset() {
        super.reset()

        customHosts.clear()
        customHosts.addAll(settings.customHosts)
    }

    override fun isModified() : Boolean {
        return super.isModified() || customHosts != settings.customHosts
    }

    override fun apply() {
        super.apply()

        settings.customHosts = customHosts
    }
}