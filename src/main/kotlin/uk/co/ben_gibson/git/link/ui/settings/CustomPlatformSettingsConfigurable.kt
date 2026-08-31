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
import javax.swing.ListSelectionModel.SINGLE_SELECTION
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.extension.replaceAt
import uk.co.ben_gibson.git.link.platform.Custom
import uk.co.ben_gibson.git.link.platform.CustomPlatform
import uk.co.ben_gibson.git.link.platform.Domain
import uk.co.ben_gibson.git.link.platform.GitHub
import uk.co.ben_gibson.git.link.platform.PlatformRepository
import uk.co.ben_gibson.git.link.platform.TemplatedPlatform
import uk.co.ben_gibson.git.link.ui.components.PlatformCellRenderer
import uk.co.ben_gibson.git.link.ui.components.SubstitutionReferenceTable
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import java.util.UUID
import uk.co.ben_gibson.git.link.ui.validation.*

class CustomPlatformSettingsConfigurable : BoundConfigurable(message("settings.custom-platform.group.title")) {
    private val platforms = service<PlatformRepository>()
    private var customPlatforms = platforms.getCustomPlatforms()
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

    private fun createTableModel(): ListTableModel<CustomPlatform> = ListTableModel(
        arrayOf(
            createColumn(message("settings.custom-platform.table.column.name")) { customPlatform -> customPlatform?.name },
            createColumn(message("settings.custom-platform.table.column.domain")) { customPlatform -> customPlatform?.domain?.toString() },
        ),
        customPlatforms
    )

    private fun createColumn(name: String, formatter: (CustomPlatform?) -> String?) : ColumnInfo<CustomPlatform, String> {
        return object : ColumnInfo<CustomPlatform, String>(name) {
            override fun valueOf(item: CustomPlatform?): String? {
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

        val dialog = CustomPlatformDialog(row)

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

        customPlatforms = platforms.getCustomPlatforms()
        refreshTableModel()
    }

    override fun isModified() : Boolean {
        return super.isModified() || customPlatforms != platforms.getCustomPlatforms()
    }

    override fun apply() {
        super.apply()

        platforms.saveCustomPlatforms(customPlatforms)
    }
}

private class CustomPlatformDialog(existing: CustomPlatform? = null) : DialogWrapper(false) {
    private val id = existing?.id ?: UUID.randomUUID()
    private var name = existing?.name ?: ""
    private var domain = existing?.domain?.toString() ?: ""
    private var fileAtBranchTemplate = ""
    private var fileAtCommitTemplate = ""
    private var commitTemplate = ""

    val platform get() = CustomPlatform(
        id,
        name,
        Domain.of(domain),
        UrlTemplates(fileAtBranchTemplate, fileAtCommitTemplate, commitTemplate)
    )

    private val substitutionReferenceTable = SubstitutionReferenceTable().apply { setShowColumns(true) }

    // Only a platform whose URL format is expressed purely as templates can seed a custom one, which
    // rules out Azure and Bitbucket Server. Another custom platform is excluded as a preset is meant to
    // be a starting point, not a copy of something the user has already written.
    private val presets = service<PlatformRepository>()
        .getAll()
        .filterIsInstance<TemplatedPlatform>()
        .filterNot { it is Custom }
        .sortedBy { it.name }

    private val presetComboBoxModel = CollectionComboBoxModel(
        presets,
        if (existing == null) presets.firstOrNull { it is GitHub } else null
    )

    private lateinit var fileAtBranchTemplateField: JBTextField
    private lateinit var fileAtCommitTemplateField: JBTextField
    private lateinit var commitTemplateField: JBTextField

    init {
        title = existing
            ?.let { message("settings.custom-platform.dialog.title.edit") }
            ?: message("settings.custom-platform.dialog.title.add")
        setOKButtonText(existing?.let { message("actions.update") } ?: message("actions.add"))
        setSize(700, 700)

        // Fill the templates before the panel binds, so the fields open populated rather than empty. An
        // existing platform keeps its own, while a new one is seeded from the preset selected below.
        val templates = existing?.templates ?: presetComboBoxModel.selected?.templates

        templates?.let {
            fileAtBranchTemplate = it.fileAtBranch
            fileAtCommitTemplate = it.fileAtCommit
            commitTemplate = it.commit
        }

        init()
    }

    override fun createCenterPanel() = panel {
        row(message("settings.custom-platform.dialog.field.name.label")) {
            textField()
                .bindText(::name)
                .validationOnApply { notBlank(it.text) ?: alphaNumeric(it.text) ?: length(it.text, 3, 15) }
                .align(AlignX.FILL)
                .resizableColumn()
                .gap(RightGap.SMALL)
                .applyToComponent { emptyText.text = message("settings.custom-platform.dialog.field.name.placeholder") }
            contextHelp(message("settings.custom-platform.dialog.field.name.help"))
        }
        row(message("settings.custom-platform.dialog.field.domain.label")) {
            textField()
                .bindText(::domain)
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
                .bindText(::fileAtBranchTemplate)
                .validationOnApply { notBlank(it.text) ?: fileAtBranchTemplate(it.text) }
                .align(AlignX.FILL)
                .resizableColumn()
                .gap(RightGap.SMALL)
                .applyToComponent { fileAtBranchTemplateField = this }
            contextHelp(message("settings.custom-platform.dialog.field.file-at-branch-template.help"))
        }
        row(message("settings.custom-platform.dialog.field.file-at-commit-template.label")) {
            textField()
                .bindText(::fileAtCommitTemplate)
                .validationOnApply { notBlank(it.text) ?: fileAtCommitTemplate(it.text) }
                .align(AlignX.FILL)
                .resizableColumn()
                .gap(RightGap.SMALL)
                .applyToComponent { fileAtCommitTemplateField = this }
            contextHelp(message("settings.custom-platform.dialog.field.file-at-commit-template.help"))
        }
        row(message("settings.custom-platform.dialog.field.commit-template.label")) {
            textField()
                .bindText(::commitTemplate)
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

    private fun applyPreset(preset: TemplatedPlatform) {
        val templates = preset.templates

        fileAtBranchTemplateField.text = templates.fileAtBranch
        fileAtCommitTemplateField.text = templates.fileAtCommit
        commitTemplateField.text = templates.commit
    }
}
