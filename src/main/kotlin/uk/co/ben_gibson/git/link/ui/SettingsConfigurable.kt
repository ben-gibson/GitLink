package uk.co.ben_gibson.git.link.ui

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.layout.panel
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.Settings
import uk.co.ben_gibson.git.link.git.RemoteHost
import javax.swing.DefaultComboBoxModel

class SettingsConfigurable(project : Project) : BoundConfigurable("Foo Bar", "Test") {
    private val settings = project.service<Settings>()

    override fun createPanel() = panel {
        row(message("settings.remote-host.label")) {
            comboBox(
                DefaultComboBoxModel(RemoteHost.values()),
                renderer = SimpleListCellRenderer.create("") { it?.displayName ?: "" },
                prop = settings::remoteHost
            )
        }
        row(message("settings.default-branch.label")) {
            textField(settings::defaultBranch)
        }
        row(message("settings.remote.label")) {
            textField(settings::remote)
        }
        titledRow("Advanced") {
            row(message("settings.check-commit-on-remote.label")) {
                checkBox(
                    message("settings.check-commit-on-remote.label"),
                    settings::checkCommitOnRemote,
                    comment = "foo bar baz"
                )
            }
            row(message("settings.force-https.label")) {
                checkBox(message("settings.force-https.label"), settings::forceHttps)
            }
        }
        row() {
            browserLink("Report a bug", "https://github.com/ben-gibson/GitLink/issues")
        }
    }
}