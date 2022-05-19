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
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.git.HostsProvider
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.components.HostCellRenderer
import uk.co.ben_gibson.git.link.ui.components.HostComboBoxModelProvider
import uk.co.ben_gibson.git.link.ui.layout.reportBugLink
import uk.co.ben_gibson.git.link.ui.validation.domain
import uk.co.ben_gibson.git.link.ui.validation.exists
import uk.co.ben_gibson.git.link.ui.validation.notBlank
import java.awt.event.ItemEvent
import java.net.URI
import javax.swing.ListSelectionModel

class AutoDetectSettings : BoundConfigurable(GitLinkBundle.message("settings.auto-detect.group.title")) {
    private val settings = service<ApplicationSettings>();
    private val hosts = service<HostsProvider>().provide();
    private var customDomains = settings.customHostDomains

    private val hostsComboBoxModel = HostComboBoxModelProvider.provide()

    private val domainsTableModel = createDomainsTableModel()

    private val domainsTable = TableView(domainsTableModel).apply {
        setShowColumns(true)
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        emptyText.text = message("settings.auto-detect.table.empty")
    }

    private val domainsTableContainer = ToolbarDecorator.createDecorator(domainsTable)
        .setAddAction { addDomain() }
        .setEditAction { editDomain() }
        .setRemoveAction { removeDomain() }
        .setEditActionUpdater { canModifyDomain() }
        .setRemoveActionUpdater() { canModifyDomain() }
        .createPanel()

    override fun createPanel() = panel {
        row(message("settings.general.field.host.label")) {
            comboBox(
                hostsComboBoxModel,
                { hosts.toSet().first() },
                { },
                HostCellRenderer()
            ).component.addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
                    val selectedHost = it.itemSelectable.selectedObjects.first() as Host
                    refreshDomainsTable(selectedHost)
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

    private fun canModifyDomain() = hostsComboBoxModel.selected?.domains?.map { it.toString() }?.contains(domainsTable.selectedObject) == false

    private fun addDomain() {
        val host = hostsComboBoxModel.selected ?: return
        val dialog = RegisterDomainDialog(existing = customDomains.getOrDefault(host.id.toString(), setOf()))

        if (dialog.showAndGet()) {
            val existingUrls = customDomains.getOrDefault(host.id.toString(), setOf())
            customDomains = customDomains.plus(Pair(host.id.toString(), existingUrls.plus(dialog.domain)))
            refreshDomainsTable(host)
        }
    }

    private fun removeDomain() {
        val host = hostsComboBoxModel.selected ?: return
        val remove = domainsTable.selectedObject ?: return

        val existingUrls = customDomains.getOrDefault(host.id.toString(), setOf())

        customDomains = customDomains.plus(Pair(host.id.toString(), existingUrls.minus(remove)))

        refreshDomainsTable(host)
    }

    private fun editDomain() {
        val host = hostsComboBoxModel.selected ?: return
        val domain = domainsTable.selectedObject ?: return

        val dialog = RegisterDomainDialog(domain, customDomains.getOrDefault(host.id.toString(), setOf()))

        if (dialog.showAndGet()) {
            val existingUrls = customDomains.getOrDefault(host.id.toString(), setOf())

            customDomains = customDomains.plus(Pair(host.id.toString(), existingUrls.minus(domain).plus(dialog.domain)))

            refreshDomainsTable(host)
        }
    }

    private fun refreshDomainsTable(host: Host) {
        domainsTableModel.items = host.domains.map { it.toString() }.toList() + customDomains.getOrDefault(host.id.toString(), setOf())
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

        customDomains = settings.customHostDomains

        val host = hostsComboBoxModel.selected ?: return

        refreshDomainsTable(host)
    }

    override fun isModified() : Boolean {
        return super.isModified() || customDomains != settings.customHostDomains
    }

    override fun apply() {
        super.apply()

        settings.customHostDomains = customDomains
    }
}

class RegisterDomainDialog(var domain: String = "", private val existing: Set<String>) : DialogWrapper(false) {
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
                .withValidationOnApply { notBlank(it.text) ?: domain(it.text) ?: exists(it.text, existing ) }
        }
    }
}
