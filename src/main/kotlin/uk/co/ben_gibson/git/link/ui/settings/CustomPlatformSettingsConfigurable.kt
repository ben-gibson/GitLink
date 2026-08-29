package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenItemSelectedFromUi
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ApplicationSettings.CustomHostSettings
import javax.swing.ListSelectionModel.SINGLE_SELECTION
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.extension.replaceAt
import uk.co.ben_gibson.git.link.platform.GitHub
import uk.co.ben_gibson.git.link.platform.Platform
import uk.co.ben_gibson.git.link.platform.PlatformRepository
import uk.co.ben_gibson.git.link.ui.components.PlatformCellRenderer
import uk.co.ben_gibson.git.link.ui.components.SubstitutionReferenceTable
import uk.co.ben_gibson.git.link.url.factory.PLATFORM_MAP
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
            cell(tableContainer)
                .align(Align.FILL)
        }.resizableRow()
        row {
            browserLink(message("actions.report-bug.title"), GitLinkBundle.URL_BUG_REPORT)
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

    // Only platforms whose URL format is expressed purely as templates can seed a custom one. Azure, Chromium and
    // Bitbucket Server are deliberately absent, as their factories do work beyond substitution that we cannot copy.
    private val presets = service<PlatformRepository>()
        .getAll()
        .filter { PLATFORM_MAP.containsKey(it::class.java) }
        .sortedBy { it.name }

    private val presetComboBoxModel = CollectionComboBoxModel(
        presets,
        if (customPlatform == null) presets.firstOrNull { it is GitHub } else null
    )

    private lateinit var fileAtBranchTemplateField: JBTextField
    private lateinit var fileAtCommitTemplateField: JBTextField
    private lateinit var commitTemplateField: JBTextField

    init {
        title = customPlatform
            ?.let { message("settings.custom-platform.dialog.title.edit") }
            ?: message("settings.custom-platform.dialog.title.add")
        setOKButtonText(customPlatform?.let { message("actions.update") } ?: message("actions.add"))
        setSize(700, 700)

        // Seed a new platform before the panel binds, so the fields open populated rather than empty.
        presetComboBoxModel.selected?.let {
            val templates = PLATFORM_MAP.getValue(it::class.java)

            platform.fileAtBranchTemplate = templates.fileAtBranch
            platform.fileAtCommitTemplate = templates.fileAtCommit
            platform.commitTemplate = templates.commit
        }

        init()
    }

    override fun createCenterPanel() = panel {
        row(message("settings.custom-platform.dialog.field.name.label")) {
            textField()
                .bindText(platform::displayName)
                .validationOnApply { notBlank(it.text) ?: alphaNumeric(it.text) ?: length(it.text, 3, 15) }
                .align(AlignX.FILL)
                .resizableColumn()
                .gap(RightGap.SMALL)
                .applyToComponent { emptyText.text = message("settings.custom-platform.dialog.field.name.placeholder") }
            contextHelp(message("settings.custom-platform.dialog.field.name.help"))
        }
        row(message("settings.custom-platform.dialog.field.domain.label")) {
            textField()
                .bindText(platform::baseUrl)
                .validationOnApply { notBlank(it.text) ?: domain(it.text) }
                .align(AlignX.FILL)
                .resizableColumn()
                .gap(RightGap.SMALL)
                .applyToComponent { emptyText.text = message("settings.custom-platform.dialog.field.domain.placeholder") }
            contextHelp(message("settings.custom-platform.dialog.field.domain.help"))
        }
        row(message("settings.custom-platform.dialog.field.copy-templates.label")) {
            comboBox(presetComboBoxModel, PlatformCellRenderer())
                .whenItemSelectedFromUi(disposable) { applyPreset(it) }
                .gap(RightGap.SMALL)
            contextHelp(message("settings.custom-platform.dialog.field.copy-templates.help"))
        }
        row(message("settings.custom-platform.dialog.field.file-at-branch-template.label")) {
            textField()
                .bindText(platform::fileAtBranchTemplate)
                .validationOnApply { notBlank(it.text) ?: fileAtBranchTemplate(it.text) }
                .align(AlignX.FILL)
                .resizableColumn()
                .gap(RightGap.SMALL)
                .applyToComponent { fileAtBranchTemplateField = this }
            contextHelp(message("settings.custom-platform.dialog.field.file-at-branch-template.help"))
        }
        row(message("settings.custom-platform.dialog.field.file-at-commit-template.label")) {
            textField()
                .bindText(platform::fileAtCommitTemplate)
                .validationOnApply { notBlank(it.text) ?: fileAtCommitTemplate(it.text) }
                .align(AlignX.FILL)
                .resizableColumn()
                .gap(RightGap.SMALL)
                .applyToComponent { fileAtCommitTemplateField = this }
            contextHelp(message("settings.custom-platform.dialog.field.file-at-commit-template.help"))
        }
        row(message("settings.custom-platform.dialog.field.commit-template.label")) {
            textField()
                .bindText(platform::commitTemplate)
                .validationOnApply { notBlank(it.text) ?: commitTemplate(it.text) }
                .align(AlignX.FILL)
                .resizableColumn()
                .gap(RightGap.SMALL)
                .applyToComponent { commitTemplateField = this }
            contextHelp(message("settings.custom-platform.dialog.field.commit-template.help"))
        }
        row {
            scrollCell(substitutionReferenceTable)
                .align(Align.FILL)
        }.resizableRow()
    }

    private fun applyPreset(preset: Platform) {
        val templates = PLATFORM_MAP.getValue(preset::class.java)

        fileAtBranchTemplateField.text = templates.fileAtBranch
        fileAtCommitTemplateField.text = templates.fileAtCommit
        commitTemplateField.text = templates.commit
    }
}
