package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.application.ApplicationManager
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
import uk.co.ben_gibson.git.link.ui.components.PlatformCellRenderer
import uk.co.ben_gibson.git.link.ui.validation.notBlank

class ProjectSettingsConfigurable(project : Project) : BoundConfigurable(message("settings.general.group.title")) {
    private val platforms = service<PlatformRepository>()
    private val settings = project.service<ProjectSettings>()
    private val platformComboBoxModel = CollectionComboBoxModel(platforms.getAll().toList())
    private val selectedPlatform get() = settings.host?.let { platforms.getById(it) }

    override fun createPanel() = panel {
        ApplicationManager.getApplication()
            .messageBus
            .connect(disposable!!)
            .subscribe(PlatformRepository.ChangeListener.TOPIC, PlatformRepository.ChangeListener { refreshPlatforms() })

        row(message("settings.general.field.platform.label")) {
            comboBox(platformComboBoxModel, PlatformCellRenderer())
                .bindItem({ selectedPlatform }, { settings.host = it?.id?.toString() })
                .gap(RightGap.SMALL)
            contextHelp(message("settings.general.field.platform.help"))
        }
        row(message("settings.general.field.fallback-branch.label")) {
            textField()
                .bindText(settings::fallbackBranch)
                .validationOnApply { notBlank(it.text) }
                .gap(RightGap.SMALL)
            contextHelp(message("settings.general.field.fallback-branch.help"))
        }
        row(message("settings.general.field.remote.label")) {
            textField()
                .bindText(settings::remote)
                .validationOnApply { notBlank(it.text) }
                .gap(RightGap.SMALL)
            contextHelp(message("settings.general.field.remote.help"))
        }
        group(message("settings.general.section.advanced.label")) {
            row {
                checkBox(message("settings.general.field.force-https.label"))
                    .bindSelected(settings::forceHttps)
                    .gap(RightGap.SMALL)
                contextHelp(message("settings.general.field.force-https.help"))
            }
            row {
                checkBox(message("settings.general.field.check-remote.label"))
                    .bindSelected(settings::shouldCheckRemote)
                    .gap(RightGap.SMALL)
                contextHelp(message("settings.general.field.check-remote.help"))
            }
        }
        row {
            browserLink(message("actions.report-bug.title"), GitLinkBundle.URL_BUG_REPORT)
        }
    }

    private fun refreshPlatforms() {
        val current = platformComboBoxModel.selectedItem as? Platform

        platformComboBoxModel.removeAll()
        platformComboBoxModel.add(platforms.getAll().toList())

        // A custom platform may have been removed while it was the selected one, leaving us pointing at a
        // platform that no longer exists.
        if (selectedPlatform == null) {
            settings.host = null
        }

        platformComboBoxModel.selectedItem = current?.let { platforms.getById(it.id) }
    }
}
