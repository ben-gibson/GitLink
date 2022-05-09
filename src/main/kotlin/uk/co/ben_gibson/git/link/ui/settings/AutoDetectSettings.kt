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
import uk.co.ben_gibson.git.link.ui.validation.exists
import uk.co.ben_gibson.git.link.ui.validation.notBlank
import uk.co.ben_gibson.git.link.ui.validation.url
import java.awt.event.ItemEvent
import java.net.URL
import javax.swing.ListSelectionModel

class AutoDetectSettings : BoundConfigurable(GitLinkBundle.message("settings.auto-detect.group.title")) {
    private val settings = service<ApplicationSettings>();
    private val hosts = service<HostsProvider>().provide();
    private var customBaseUrls = settings.customBaseUrls

    private val hostsComboBoxModel = HostComboBoxModelProvider.provide()

    private val customBaseUrlTableModel = createCustomBaseUrlTableModel()

    private val customBaseUrlTable = TableView(customBaseUrlTableModel).apply {
        setShowColumns(true)
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        emptyText.text = "No custom base URLs"
    }

    private val customBaseUrlTableContainer = ToolbarDecorator.createDecorator(customBaseUrlTable)
        .setAddAction { addBaseUrl() }
        .setEditAction { editBaseUrl() }
        .setRemoveAction { removeBaseUrl() }
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
                    refreshCustomBaseUrlsTable(selectedHost)
                }
            }
        }
        row {
            component(customBaseUrlTableContainer).constraints(CCFlags.grow)
        }
        row {
            reportBugLink()
        }
    }

    private fun addBaseUrl() {
        val host = hostsComboBoxModel.selected ?: return
        val dialog = BaseUrlDialog(existing = customBaseUrls.getOrDefault(host.id.toString(), setOf()))

        if (dialog.showAndGet()) {
            val existingUrls = customBaseUrls.getOrDefault(host.id.toString(), setOf())
            customBaseUrls = customBaseUrls.plus(Pair(host.id.toString(), existingUrls.plus(dialog.result)))
            refreshCustomBaseUrlsTable(host)
        }
    }

    private fun removeBaseUrl() {
        val host = hostsComboBoxModel.selected ?: return
        val remove = customBaseUrlTable.selectedObject ?: return

        val existingUrls = customBaseUrls.getOrDefault(host.id.toString(), setOf())

        customBaseUrls = customBaseUrls.plus(Pair(host.id.toString(), existingUrls.minus(remove)))

        refreshCustomBaseUrlsTable(host)
    }

    private fun editBaseUrl() {
        val host = hostsComboBoxModel.selected ?: return
        val url = customBaseUrlTable.selectedObject ?: return

        val dialog = BaseUrlDialog(url, customBaseUrls.getOrDefault(host.id.toString(), setOf()))

        if (dialog.showAndGet()) {
            val existingUrls = customBaseUrls.getOrDefault(host.id.toString(), setOf())

            customBaseUrls = customBaseUrls.plus(Pair(host.id.toString(), existingUrls.minus(url).plus(dialog.result)))

            refreshCustomBaseUrlsTable(host)
        }
    }

    private fun refreshCustomBaseUrlsTable(host: Host) {
        val urls = customBaseUrls.getOrDefault(host.id.toString(), setOf())
        customBaseUrlTableModel.items = urls.toList()
    }

    private fun createCustomBaseUrlTableModel(urls: List<URL> = listOf()): ListTableModel<URL> = ListTableModel(
        arrayOf(
            object : ColumnInfo<URL, URL>("Base URL") {
                override fun valueOf(url: URL): URL {
                    return url
                }
            }
        ),
        urls
    )

    override fun reset() {
        super.reset()

        customBaseUrls = settings.customBaseUrls

        val host = hostsComboBoxModel.selected ?: return

        refreshCustomBaseUrlsTable(host)
    }

    override fun isModified() : Boolean {
        return super.isModified() || customBaseUrls != settings.customBaseUrls
    }

    override fun apply() {
        super.apply()

        settings.customBaseUrls = customBaseUrls
    }
}

class BaseUrlDialog(private val baseUrl: URL? = null, private val existing: Collection<URL>) : DialogWrapper(false) {
    val result: URL get() = URL(textValue)

    private var textValue = baseUrl?.toString() ?: ""

    init {
        title = "Add Base URL"
        setOKButtonText(message("actions.add"))
        init()
    }

    override fun createCenterPanel() = panel {
        row("Base URL") {
            textField(::textValue)
                .focused()
                .withValidationOnApply { notBlank(it.text) ?: url(it.text) ?: exists(it.text, existing.map { url -> url.toString() } ) }
        }
    }
}
