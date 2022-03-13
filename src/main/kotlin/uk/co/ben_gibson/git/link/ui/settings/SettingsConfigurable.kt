package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.fields.ExtendableTextComponent
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.Settings
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.git.HostsProvider
import javax.swing.DefaultComboBoxModel
import javax.swing.JList
import javax.swing.JTextField
import javax.swing.plaf.basic.BasicComboBoxEditor


class SettingsConfigurable(project : Project) : BoundConfigurable("Foo Bar", "Test") {
    private val settings = project.service<Settings>()
    private val hosts = project.service<HostsProvider>().provide()

    private val addCustomHostExtension = ExtendableTextComponent.Extension.create(
        AllIcons.General.Add,
        AllIcons.General.InlineAddHover,
    "Add Custom Host"
    ) {
        if (CustomHostDialog().showAndGet()) {
            // user pressed OK
        }
    }

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
        row() {
            browserLink("Report a bug", "https://github.com/ben-gibson/GitLink/issues")
        }
    }
}