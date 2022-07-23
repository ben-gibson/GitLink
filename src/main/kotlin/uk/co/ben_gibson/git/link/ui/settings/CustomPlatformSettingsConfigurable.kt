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
import uk.co.ben_gibson.git.link.extension.replaceAt
import uk.co.ben_gibson.git.link.ui.components.SubstitutionReferenceTable
import uk.co.ben_gibson.git.link.ui.layout.reportBugLink
import uk.co.ben_gibson.git.link.ui.validation.*

class CustomPlatformSettingsConfigurable : BoundConfigurable(message("settings.custom-platform.group.title")) {
    private var settings = service<ApplicationSettings>()
    private var customPlatforms = settings.customHosts
    private val tableModel = createTableModel()

    private val table = TableView(tableModel).apply {
        setShowColumns(true)
        setSelectionMode(SINGLE_SELECTION)
        emptyText.text = message("settings.custom-platform.table.empty")
    }

    private val tableContainer = ToolbarDecorator.createDecorator(table)
        .setAddAction { addCustomPlatform() }
        .setEditAction { editCustomPlatform() }
        .setRemoveAction { removeCustomPlatform() }
        .createPanel()

    override fun createPanel() = panel {
        row {
            component(tableContainer).constraints(CCFlags.grow)
        }
        row {
            reportBugLink()
        }
    }

    private fun createTableModel(): ListTableModel<CustomHostSettings> = ListTableModel(
        arrayOf(
            createColumn(message("settings.custom-platform.table.column.name")) { customPlatform -> customPlatform?.displayName },
            createColumn(message("settings.custom-platform.table.column.domain")) { customPlatform -> customPlatform?.baseUrl },
        ),
        customPlatforms
    )

    private fun createColumn(name: String, formatter: (CustomHostSettings?) -> String?) : ColumnInfo<CustomHostSettings, String> {
        return object : ColumnInfo<CustomHostSettings, String>(name) {
            override fun valueOf(item: CustomHostSettings?): String? {
                return formatter(item)
            }
        }
    }

    private fun addCustomPlatform() {
        val dialog = CustomPlatformDialog()

        if (dialog.showAndGet()) {
            customPlatforms = customPlatforms.plus(dialog.platform)
            refreshTableModel()
        }
    }

    private fun removeCustomPlatform() {
        val row = table.selectedObject ?: return

        customPlatforms = customPlatforms.minus(row)
        refreshTableModel()
    }

    private fun editCustomPlatform() {
        val row = table.selectedObject ?: return

        val dialog = CustomPlatformDialog(row.copy())

        if (dialog.showAndGet()) {
            customPlatforms = customPlatforms.replaceAt(table.selectedRow, dialog.platform)
            refreshTableModel()
        }
    }

    private fun refreshTableModel() {
        tableModel.items = customPlatforms
    }

    override fun reset() {
        super.reset()

        customPlatforms = settings.customHosts
        refreshTableModel()
    }

    override fun isModified() : Boolean {
        return super.isModified() || customPlatforms != settings.customHosts
    }

    override fun apply() {
        super.apply()

        settings.customHosts = customPlatforms
    }
}

private class CustomPlatformDialog(customPlatform: CustomHostSettings? = null) : DialogWrapper(false) {
    val platform = customPlatform ?: CustomHostSettings()
    private val substitutionReferenceTable = SubstitutionReferenceTable().apply { setShowColumns(true) }

    init {
        title = message("settings.custom-platform.add-dialog.title")
        setOKButtonText(customPlatform?.let { message("actions.update") } ?: message("actions.add"))
        setSize(700, 700)
        init()
    }

    override fun createCenterPanel() = panel {
        row(message("settings.custom-platform.add-dialog.field.name.label")) {
            textField(platform::displayName)
                .focused()
                .withValidationOnApply { notBlank(it.text) ?: alphaNumeric(it.text) ?: length(it.text, 3, 15) }
                .comment(message("settings.custom-platform.add-dialog.field.name.comment"))
        }
        row(message("settings.custom-platform.add-dialog.field.domain.label")) {
            textField(platform::baseUrl)
                .withValidationOnApply { notBlank(it.text) ?: domain(it.text) }
                .comment(message("settings.custom-platform.add-dialog.field.domain.comment"))
        }
        row(message("settings.custom-platform.add-dialog.field.file-at-branch-template.label")) {
            textField(platform::fileAtBranchTemplate)
                .withValidationOnApply { notBlank(it.text) ?: fileAtBranchTemplate(it.text) }
                .comment(message("settings.custom-platform.add-dialog.field.file-at-branch-template.comment"))
        }
        row(message("settings.custom-platform.add-dialog.field.file-at-commit-template.label")) {
            textField(platform::fileAtCommitTemplate)
                .withValidationOnApply { notBlank(it.text) ?: fileAtCommitTemplate(it.text) }
                .comment(message("settings.custom-platform.add-dialog.field.file-at-commit-template.comment"))
        }
        row(message("settings.custom-platform.add-dialog.field.commit-template.label")) {
            textField(platform::commitTemplate)
                .withValidationOnApply { notBlank(it.text) ?: commitTemplate(it.text) }
                .comment(message("settings.custom-platform.add-dialog.field.commit-template.comment"))
        }
        row() {
            scrollPane(substitutionReferenceTable)
        }
    }
}