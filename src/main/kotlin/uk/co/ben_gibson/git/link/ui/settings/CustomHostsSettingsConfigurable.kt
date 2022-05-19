package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ApplicationSettings.CustomHostSettings
import javax.swing.ListSelectionModel.SINGLE_SELECTION
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.extensions.replaceAt
import uk.co.ben_gibson.git.link.ui.components.SubstitutionReferenceTable
import uk.co.ben_gibson.git.link.ui.layout.reportBugLink
import uk.co.ben_gibson.git.link.ui.validation.*

class CustomHostsSettingsConfigurable : BoundConfigurable(message("settings.custom-host.group.title")) {
    private var settings = service<ApplicationSettings>()
    private var customHosts = settings.customHosts
    private val customHostTableModel = createCustomHostModel()

    private val customHostsTable = TableView(customHostTableModel).apply {
        setShowColumns(true)
        setSelectionMode(SINGLE_SELECTION)
        emptyText.text = message("settings.custom-host.table.empty")
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
            reportBugLink()
        }
    }

    private fun createCustomHostModel(): ListTableModel<CustomHostSettings> = ListTableModel(
        arrayOf(
            createCustomHostColumn(message("settings.custom-host.table.column.name")) { customHost -> customHost?.displayName },
            createCustomHostColumn(message("settings.custom-host.table.column.domain")) { customHost -> customHost?.baseUrl },
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
        val dialog = CustomHostDialog()

        if (dialog.showAndGet()) {
            customHosts = customHosts.plus(dialog.host)
            refreshCustomHostTableModel()
        }
    }

    private fun removeCustomHost() {
        val row = customHostsTable.selectedObject ?: return

        customHosts = customHosts.minus(row)
        refreshCustomHostTableModel()
    }

    private fun editCustomHost() {
        val row = customHostsTable.selectedObject ?: return

        val dialog = CustomHostDialog(row.copy())

        if (dialog.showAndGet()) {
            customHosts = customHosts.replaceAt(customHostsTable.selectedRow, dialog.host)
            refreshCustomHostTableModel()
        }
    }

    private fun refreshCustomHostTableModel() {
        customHostTableModel.items = customHosts
    }

    override fun reset() {
        super.reset()

        customHosts = settings.customHosts
        refreshCustomHostTableModel()
    }

    override fun isModified() : Boolean {
        return super.isModified() || customHosts != settings.customHosts
    }

    override fun apply() {
        super.apply()

        settings.customHosts = customHosts
    }
}

private class CustomHostDialog(private val customHost: CustomHostSettings? = null) : DialogWrapper(false) {
    val host = customHost ?: CustomHostSettings()
    private val substitutionReferenceTable = SubstitutionReferenceTable().apply { setShowColumns(true) }

    init {
        title = message("settings.custom-host.add-dialog.title")
        setOKButtonText(customHost?.let { message("actions.update") } ?: message("actions.add"))
        setSize(700, 700)
        init()
    }

    override fun createCenterPanel() = panel {
        row(message("settings.custom-host.add-dialog.field.name.label")) {
            textField(host::displayName)
                .focused()
                .withValidationOnApply { notBlank(it.text) ?: alphaNumeric(it.text) ?: length(it.text, 3, 15) }
                .comment(message("settings.custom-host.add-dialog.field.name.comment"))
        }
        row(message("settings.custom-host.add-dialog.field.domain.label")) {
            textField(host::baseUrl)
                .withValidationOnApply { notBlank(it.text) ?: domain(it.text) }
                .comment(message("settings.custom-host.add-dialog.field.domain.comment"))
        }
        row(message("settings.custom-host.add-dialog.field.file-at-branch-template.label")) {
            textField(host::fileAtBranchTemplate)
                .withValidationOnApply { notBlank(it.text) ?: fileAtBranchTemplate(it.text) }
                .comment(message("settings.custom-host.add-dialog.field.file-at-branch-template.comment"))
        }
        row(message("settings.custom-host.add-dialog.field.file-at-commit-template.label")) {
            textField(host::fileAtCommitTemplate)
                .withValidationOnApply { notBlank(it.text) ?: fileAtCommitTemplate(it.text) }
                .comment(message("settings.custom-host.add-dialog.field.file-at-commit-template.comment"))
        }
        row(message("settings.custom-host.add-dialog.field.commit-template.label")) {
            textField(host::commitTemplate)
                .withValidationOnApply { notBlank(it.text) ?: commitTemplate(it.text) }
                .comment(message("settings.custom-host.add-dialog.field.commit-template.comment"))
        }
        row() {
            scrollPane(substitutionReferenceTable)
        }
    }
}