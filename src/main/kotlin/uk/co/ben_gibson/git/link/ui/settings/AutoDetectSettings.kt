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
import uk.co.ben_gibson.git.link.url.domain
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
        emptyText.text = "No domains registered"
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

    private fun canModifyDomain() = hostsComboBoxModel.selected?.domains?.contains(domainsTable.selectedObject) != true

    private fun addDomain() {
        val host = hostsComboBoxModel.selected ?: return
        val dialog = RegisterDomainDialog(existing = customDomains.getOrDefault(host.id.toString(), setOf()))

        if (dialog.showAndGet()) {
            val existingUrls = customDomains.getOrDefault(host.id.toString(), setOf())
            customDomains = customDomains.plus(Pair(host.id.toString(), existingUrls.plus(dialog.result)))
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
        val url = domainsTable.selectedObject ?: return

        val dialog = RegisterDomainDialog(url, customDomains.getOrDefault(host.id.toString(), setOf()))

        if (dialog.showAndGet()) {
            val existingUrls = customDomains.getOrDefault(host.id.toString(), setOf())

            customDomains = customDomains.plus(Pair(host.id.toString(), existingUrls.minus(url).plus(dialog.result)))

            refreshDomainsTable(host)
        }
    }

    private fun refreshDomainsTable(host: Host) {
        domainsTableModel.items = host.domains.toList() + customDomains.getOrDefault(host.id.toString(), setOf())
    }

    private fun createDomainsTableModel(domains: List<URI> = listOf()): ListTableModel<URI> = ListTableModel(
        arrayOf(
            object : ColumnInfo<URI, URI>("Domain") {
                override fun valueOf(domain: URI): URI {
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

class RegisterDomainDialog(private val domain: URI? = null, private val existing: Collection<URI>) : DialogWrapper(false) {
    val result: URI get() = URI(textValue).domain

    private var textValue = domain?.toString() ?: ""

    init {
        title = "Register Domain"
        setOKButtonText(message("actions.register"))
        setSize(600, 100)
        init()
    }

    override fun createCenterPanel() = panel {
        row("Register Domain") {
            textField(::textValue)
                .focused()
                .withValidationOnApply { notBlank(it.text) ?: domain(it.text) ?: exists(it.text, existing.map { domain -> domain.toString() } ) }
        }
    }
}
