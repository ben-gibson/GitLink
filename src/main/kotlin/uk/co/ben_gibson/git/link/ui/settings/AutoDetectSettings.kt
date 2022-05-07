package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.SimpleListCellRenderer
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
import java.awt.event.ItemEvent
import java.net.URL
import javax.swing.JList
import javax.swing.ListSelectionModel

class AutoDetectSettings : BoundConfigurable(GitLinkBundle.message("settings.auto-detect.group.title")) {
    private val hosts = service<HostsProvider>().provide();
    private val customBaseUrls = mapOf<String, List<URL>>();

    private val hostsComboBoxModel = CollectionComboBoxModel(hosts.toSet().toList())

    private val tableModel = createModel()

    private val table = TableView(tableModel).apply {
        setShowColumns(true)
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        emptyText.text = "No custom base URLs"
    }

    private val tableContainer = ToolbarDecorator.createDecorator(table)
        .setAddAction { }
        .setEditAction { }
        .setRemoveAction { }
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
        table.model = createModel(host.baseUrls.toList())
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
}