package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.layout.panel
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.git.HostsProvider
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import javax.swing.DefaultComboBoxModel
import javax.swing.JList

class ProjectSettingsConfigurable(project : Project) : BoundConfigurable(message("settings.general.group.title")), ApplicationSettings.ChangeListener {
    private val settings = project.service<ProjectSettings>()
    private val hostsComboBoxModel: DefaultComboBoxModel<Host>
    private val initialHost: Host?

    init {
        val hosts = service<HostsProvider>().provide()

        initialHost = settings.host?.let { hosts.getById(it) }
        hostsComboBoxModel = DefaultComboBoxModel(hosts.toArray())

        service<ApplicationSettings>().registerListener(this)
    }

    override fun createPanel() = panel {
        row(message("settings.general.field.host.label")) {
            comboBox(
                hostsComboBoxModel,
                { initialHost },
                { settings.host = it?.id?.toString() },
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
        row(message("settings.general.field.fallback-branch.label")) {
            textField(settings::fallbackBranch)
        }
        row(message("settings.general.field.remote.label")) {
            textField(settings::remote)
        }
        titledRow(message("settings.general.section.advanced.label")) {
            row(message("settings.general.field.force-https.label")) {
                checkBox(message("settings.general.field.force-https.label"), settings::forceHttps)
            }
            row(message("settings.general.field.check-commit-on-remote.label")) {
                checkBox(
                    message("settings.general.field.check-commit-on-remote.label"),
                    settings::checkCommitOnRemote,
                    comment = message("settings.general.field.check-commit-on-remote.help")
                )
            }
        }
        row {
            browserLink(message("actions.report-bug.title"), "https://github.com/ben-gibson/GitLink/issues")
        }
    }

    override fun onChange() {
        val current = hostsComboBoxModel.selectedItem
        val updatedHosts = service<HostsProvider>().provide().toSet();

        hostsComboBoxModel.apply {
            removeAllElements()
            addAll(service<HostsProvider>().provide().toSet())

            if (updatedHosts.contains(current)) {
                selectedItem = current
            } else {
                selectedItem = null
            }
        }
    }
}