package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.platform.Platform
import uk.co.ben_gibson.git.link.platform.PlatformRepository
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.components.PlatformCellRenderer
import uk.co.ben_gibson.git.link.ui.layout.reportBugLink
import uk.co.ben_gibson.git.link.ui.validation.domain
import uk.co.ben_gibson.git.link.ui.validation.exists
import uk.co.ben_gibson.git.link.ui.validation.notBlank
import java.awt.event.ItemEvent
import javax.swing.ListSelectionModel

class DomainRegistrySettings : BoundConfigurable(message("settings.domain-registry.group.title")), ApplicationSettings.ChangeListener {
    private val settings = service<ApplicationSettings>()
    private val platforms = service<PlatformRepository>()
    private var domainRegistry = settings.customHostDomains

    private val platformComboBoxModel = CollectionComboBoxModel(platforms.getAll().toList())

    private val domainsTableModel = createDomainsTableModel()

    private val domainsTable = TableView(domainsTableModel).apply {
        setShowColumns(true)
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        emptyText.text = message("settings.domain-registry.table.empty")
    }

    private val domainsTableContainer = ToolbarDecorator.createDecorator(domainsTable)
        .setAddAction { addDomain() }
        .setEditAction { editDomain() }
        .setRemoveAction { removeDomain() }
        .setEditActionUpdater { canModifyDomain() }
        .setRemoveActionUpdater() { canModifyDomain() }
        .createPanel()

    init {
        service<ApplicationSettings>().registerListener(this)
    }

    override fun createPanel() = panel {
        row(message("settings.general.field.platform.label")) {
            comboBox(
                platformComboBoxModel,
                { platforms.getAll().first() },
                { },
                PlatformCellRenderer()
            ).component.addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
                    val selectedPlatform = it.itemSelectable.selectedObjects.first() as Platform
                    refreshDomainsTable(selectedPlatform)
                }
            }
        }
        row {
            component(domainsTableContainer).constraints(CCFlags.grow)
        }
        row {
            reportBugLink()
        }
    }

    private fun canModifyDomain() : Boolean {
        val platform = platformComboBoxModel.selectedItem as Platform? ?: return false

        return !platform.domains.map { it.toString() }.contains(domainsTable.selectedObject)
    }

    private fun addDomain() {
        val platform = platformComboBoxModel.selected ?: return
        val dialog = RegisterDomainDialog(domainsRegistry = domainRegistry, platforms = platforms)

        if (dialog.showAndGet()) {
            val domains = domainRegistry.getOrDefault(platform.id.toString(), setOf())
            domainRegistry = domainRegistry.plus(Pair(platform.id.toString(), domains.plus(dialog.domain)))
            refreshDomainsTable(platform)
        }
    }

    private fun removeDomain() {
        val platform = platformComboBoxModel.selected ?: return
        val remove = domainsTable.selectedObject ?: return

        val domains = domainRegistry.getOrDefault(platform.id.toString(), setOf())

        domainRegistry = domainRegistry.plus(Pair(platform.id.toString(), domains.minus(remove)))

        refreshDomainsTable(platform)
    }

    private fun editDomain() {
        val platform = platformComboBoxModel.selected ?: return
        val domain = domainsTable.selectedObject ?: return

        val dialog = RegisterDomainDialog(domain, domainRegistry, platforms)

        if (dialog.showAndGet()) {
            val existingUrls = domainRegistry.getOrDefault(platform.id.toString(), setOf())

            domainRegistry = domainRegistry.plus(Pair(platform.id.toString(), existingUrls.minus(domain).plus(dialog.domain)))

            refreshDomainsTable(platform)
        }
    }

    private fun refreshDomainsTable(platform: Platform) {
        domainsTableModel.items = platform.domains.map { it.toString() }.toList() + domainRegistry.getOrDefault(platform.id.toString(), setOf())
    }

    private fun createDomainsTableModel(domains: List<String> = listOf()): ListTableModel<String> = ListTableModel(
        arrayOf(
            object : ColumnInfo<String, String>(message("settings.auto-detect.table.column.domain")) {
                override fun valueOf(domain: String): String {
                    return domain
                }
            }
        ),
        domains
    )

    override fun reset() {
        super.reset()

        domainRegistry = settings.customHostDomains

        val platform = platformComboBoxModel.selected ?: return

        refreshDomainsTable(platform)
    }

    override fun isModified() : Boolean {
        return super.isModified() || domainRegistry != settings.customHostDomains
    }

    override fun apply() {
        super.apply()

        settings.customHostDomains = domainRegistry
    }

    override fun onChange() {
        val current = platformComboBoxModel.selectedItem as? Platform

        platformComboBoxModel.removeAll()
        platformComboBoxModel.add(platforms.getAll().toList())
        platformComboBoxModel.selectedItem = current?.let { platforms.getById(it.id) }
    }
}

class RegisterDomainDialog(
    var domain: String = "",
    private val domainsRegistry: Map<String, Set<String>>,
    private val platforms: PlatformRepository
) : DialogWrapper(false) {
    init {
        title = message("settings.auto-detect.register-domain-dialog.title")
        setOKButtonText(if (domain.isEmpty()) { message("actions.register") } else { message("actions.update") })
        setSize(600, 100)
        init()
    }

    override fun createCenterPanel() = panel {
        row(message("settings.auto-detect.register-domain-dialog.title")) {
            textField(::domain)
                .focused()
                .withValidationOnApply {
                    notBlank(it.text) ?:
                    domain(it.text) ?:
                    exists(it.text, platforms.getAll().flatMap { platform -> platform.domains.map { domain -> domain.toString() } } ) ?:
                    exists(it.text, domainsRegistry.values.flatten())
                }

        }
    }
}
