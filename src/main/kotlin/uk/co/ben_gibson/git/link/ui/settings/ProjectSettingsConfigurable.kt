package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.dsl.builder.*
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.platform.Platform
import uk.co.ben_gibson.git.link.platform.PlatformRepository
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.components.PlatformCellRenderer
import uk.co.ben_gibson.git.link.ui.validation.notBlank

class ProjectSettingsConfigurable(project : Project) : BoundConfigurable(message("settings.general.group.title")), ApplicationSettings.ChangeListener {
    private val platforms = service<PlatformRepository>()
    private val settings = project.service<ProjectSettings>()
    private val platformComboBoxModel = CollectionComboBoxModel(platforms.getAll().toList())
    private val initialPlatform = settings.host?.let { platforms.getById(it) }

    init {
        service<ApplicationSettings>().registerListener(this)
    }

    override fun createPanel() = panel {
        row(message("settings.general.field.platform.label")) {
            comboBox(platformComboBoxModel, PlatformCellRenderer())
                .bindItem({ initialPlatform }, { settings.host = it?.id?.toString() })
                .comment(message("settings.general.field.platform.help"))
        }
        row(message("settings.general.field.fallback-branch.label")) {
            textField()
                .bindText(settings::fallbackBranch)
                .comment(message("settings.general.field.fallback-branch.help"))
                .validationOnApply { notBlank(it.text) }
        }
        row(message("settings.general.field.remote.label")) {
            textField()
                .bindText(settings::remote)
                .validationOnApply { notBlank(it.text) }
        }
        group(message("settings.general.section.advanced.label")) {
            row {
                checkBox(message("settings.general.field.force-https.label"))
                    .bindSelected(settings::forceHttps)
            }
            row {
                checkBox(message("settings.general.field.should-check-remote.label"))
                    .comment(message("settings.general.field.check-commit-on-remote.help"))
                    .bindSelected(settings::shouldCheckRemote)
            }
        }
        row {
            browserLink(message("actions.report-bug.title"), GitLinkBundle.URL_BUG_REPORT)
        }
    }

    override fun onChange() {
        val current = platformComboBoxModel.selectedItem as? Platform

        platformComboBoxModel.removeAll()
        platformComboBoxModel.add(platforms.getAll().toList())
        platformComboBoxModel.selectedItem = current?.let { platforms.getById(it.id) }
    }
}