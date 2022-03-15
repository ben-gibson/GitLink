package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.AnActionButton
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.fields.ExtendableTextComponent
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.GrowPolicy
import com.intellij.ui.layout.panel
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ElementProducer
import com.intellij.util.ui.JBDimension
import com.intellij.util.ui.ListTableModel
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.Settings
import uk.co.ben_gibson.git.link.Settings.CustomHostSettings
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.git.HostsProvider
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.ListSelectionModel.SINGLE_SELECTION


class SettingsConfigurable(project : Project) : BoundConfigurable("Foo Bar", "Test") {
    private val settings = project.service<Settings>()
    private val hosts = project.service<HostsProvider>().provide()

    private val customHostsTable = TableView(createCustomHostModel()).apply {
        setShowColumns(true)
        setSelectionMode(SINGLE_SELECTION)
        emptyText.text = "No custom hosts"
    }

    private val customHostTableContainer = ToolbarDecorator.createDecorator(customHostsTable)
        .setAddAction { addCustomHost() }
        .setEditAction { editCustomHost() }
        .setRemoveAction { removeCustomHost() }
        .createPanel()

    override fun createPanel() = panel {
        row(message("settings.host.label")) {
            comboBox(
                DefaultComboBoxModel(hosts.toArray()),
                { hosts.getById(settings.host) },
                { settings.host = it?.id.toString() },
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
            )
        }
        row(message("settings.fallback-branch.label")) {
            textField(settings::fallbackBranch)
        }
        row(message("settings.remote.label")) {
            textField(settings::remote)
        }
        titledRow("Advanced") {
            row(message("settings.force-https.label")) {
                checkBox(message("settings.force-https.label"), settings::forceHttps)
            }
            row(message("settings.check-commit-on-remote.label")) {
                checkBox(
                    message("settings.check-commit-on-remote.label"),
                    settings::checkCommitOnRemote,
                    comment = "foo bar baz"
                )
            }
        }
        row {
            component(customHostTableContainer).constraints(CCFlags.grow)
        }
        row {
            browserLink("Report a bug", "https://github.com/ben-gibson/GitLink/issues")
        }
    }

    private fun createCustomHostModel(): ListTableModel<CustomHostSettings> = ListTableModel(
        arrayOf(
            createCustomHostColumn("Host Name") { customHost -> customHost?.displayName },
            createCustomHostColumn("Base URL") { customHost -> customHost?.baseUrl },
        ),
        settings.customHosts
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
            settings.customHosts.add(dialog.getCustomHostSettings())
        }
    }

    private fun removeCustomHost() {
        val row = customHostsTable.selectedObject ?: return

        settings.customHosts.remove(row)
        customHostsTable.tableViewModel.fireTableDataChanged()
    }

    private fun editCustomHost() {

    }
}