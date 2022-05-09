package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.panel
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.git.HostsProvider
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.components.HostCellRenderer
import uk.co.ben_gibson.git.link.ui.layout.reportBugLink
import uk.co.ben_gibson.git.link.ui.validation.notBlank
import javax.swing.DefaultComboBoxModel

class ProjectSettingsConfigurable(project : Project) : BoundConfigurable(message("settings.general.group.title")), ApplicationSettings.ChangeListener {
    private val hosts = service<HostsProvider>().provide()
    private val settings = project.service<ProjectSettings>()
    private val hostsComboBoxModel = DefaultComboBoxModel(hosts.toArray())
    private val initialHost = settings.host?.let { hosts.getById(it) }

    init {
        service<ApplicationSettings>().registerListener(this)
    }

    override fun createPanel() = panel {
        row(message("settings.general.field.host.label")) {
            comboBox(
                hostsComboBoxModel,
                { initialHost },
                { settings.host = it?.id?.toString() },
                HostCellRenderer()
            )
        }
        row(message("settings.general.field.fallback-branch.label")) {
            textField(settings::fallbackBranch)
                .comment(message("settings.general.field.fallback-branch.help"))
                .withValidationOnApply { notBlank(it.text) }
        }
        row(message("settings.general.field.remote.label")) {
            textField(settings::remote)
                .withValidationOnApply { notBlank(it.text) }
        }
        titledRow(message("settings.general.section.advanced.label")) {
            row(message("settings.general.field.force-https.label")) {
                checkBox(message("settings.general.field.force-https.label"), settings::forceHttps)
            }.largeGapAfter()
//            row("Generate URLs to current file using") {
//                buttonGroup(settings::generateUrlTo) {
//                    radioButton("Commit", ContextType.COMMIT)
//                    radioButton("Branch", ContextType.BRANCH)
//                }
//            }.largeGapAfter()
            row(message("settings.general.field.check-commit-on-remote.label")) {
                checkBox(
                    message("settings.general.field.check-commit-on-remote.label"),
                    settings::checkCommitOnRemote,
                    comment = message("settings.general.field.check-commit-on-remote.help")
                )
            }
        }
        row {
            reportBugLink()
        }
    }

    override fun onChange() {
        val current = hostsComboBoxModel.selectedItem
        val updatedHosts = service<HostsProvider>().provide().toSet();

        hostsComboBoxModel.apply {
            removeAllElements()
            addAll(service<HostsProvider>().provide().toSet())

            selectedItem = if (updatedHosts.contains(current)) {
                current
            } else {
                null
            }
        }
    }
}