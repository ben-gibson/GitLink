package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.git.HostsProvider
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import java.awt.event.ItemEvent
import java.net.MalformedURLException
import java.net.URL
import javax.swing.JList
import javax.swing.ListSelectionModel

class AutoDetectSettings : BoundConfigurable(GitLinkBundle.message("settings.auto-detect.group.title")) {
    private val settings = service<ApplicationSettings>();
    private val hosts = service<HostsProvider>().provide();
    private val customBaseUrls = settings.customBaseUrls.toMutableMap()

    private val hostsComboBoxModel = CollectionComboBoxModel(hosts.toSet().toList())

    private val tableModel = createModel()

    private val table = TableView(tableModel).apply {
        setShowColumns(true)
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        emptyText.text = "No custom base URLs"
    }

    private val tableContainer = ToolbarDecorator.createDecorator(table)
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
                object : SimpleListCellRenderer<Host>() {
                    override fun customize(
                        list: JList<out Host>,
                        value: Host?,
                        index: Int,
                        selected: Boolean,
                        hasFocus: Boolean
                    ) {
                        text = value?.displayName ?: ""
                        icon = value?.icon
                    }

                }
            ).component.addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
                    val selectedHost = it.itemSelectable.selectedObjects.first() as Host
                    updateTable(selectedHost)
                }
            }
        }
        row {
            component(tableContainer).constraints(CCFlags.grow)
        }
    }

    private fun updateTable(host: Host) {
        table.model = createModel(customBaseUrls.getOrDefault(host.id.toString(), mutableListOf()))
    }

    private fun addBaseUrl() {
        val dialog = BaseUrlDialog()
        val host = hostsComboBoxModel.selected ?: return

        if (dialog.showAndGet()) {
            customBaseUrls.putIfAbsent(host.id.toString(), mutableListOf())
            customBaseUrls[host.id.toString()]?.add(URL(dialog.baseUrl))

            table.tableViewModel.fireTableDataChanged()
        }
    }

    private fun removeBaseUrl() {
        val host = hostsComboBoxModel.selected ?: return

        customBaseUrls[host.id.toString()]?.removeAt(table.selectedRow)
        table.tableViewModel.fireTableDataChanged()
    }

    private fun editBaseUrl() {
        val dialog = BaseUrlDialog()
        val host = hostsComboBoxModel.selected ?: return

        if (dialog.showAndGet()) {
            customBaseUrls[host.id.toString()]?.add(table.selectedRow, URL(dialog.baseUrl))
            table.tableViewModel.fireTableDataChanged()
        }
    }

    private fun createModel(urls: List<URL> = listOf()): ListTableModel<URL> = ListTableModel(
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

        customBaseUrls.clear()
        customBaseUrls.putAll(settings.customBaseUrls)
    }

    override fun isModified() : Boolean {
        return super.isModified() || customBaseUrls != settings.customBaseUrls
    }

    override fun apply() {
        super.apply()

        settings.customBaseUrls = customBaseUrls
    }
}

class BaseUrlDialog(var baseUrl: String = "") : DialogWrapper(false) {
    init {
        title = "Add Base URL"
        setOKButtonText(message("actions.add"))
        init()
    }

    override fun createCenterPanel() = panel {
        row("Base URL") {
            textField(::baseUrl)
                .focused()
                .withValidationOnApply(::validateBaseUrl)
        }
    }

    private fun validateBaseUrl(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error(message("validation.required"))
                else -> {
                    try {
                        URL(fieldText)
                    } catch (e: MalformedURLException) {
                        return error(message("validation.invalid-url"))
                    }

                    return null
                }
            }
        }
    }
}
