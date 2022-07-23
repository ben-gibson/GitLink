package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.panel
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.platform.Platform
import uk.co.ben_gibson.git.link.platform.PlatformRepository
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.components.PlatformCellRenderer
import uk.co.ben_gibson.git.link.ui.layout.reportBugLink
import uk.co.ben_gibson.git.link.ui.validation.notBlank
import java.util.*
import javax.swing.DefaultComboBoxModel

class ProjectSettingsConfigurable(project : Project) : BoundConfigurable(message("settings.general.group.title")), ApplicationSettings.ChangeListener {
    private val platforms = service<PlatformRepository>()
    private val settings = project.service<ProjectSettings>()
    private val platformComboBoxModel = DefaultComboBoxModel(Vector(platforms.getAll()))
    private val initialPlatform = settings.host?.let { platforms.getById(it) }

    init {
        service<ApplicationSettings>().registerListener(this)
    }

    override fun createPanel() = panel {
        row(message("settings.general.field.platform.label")) {
            comboBox(
                platformComboBoxModel,
                { initialPlatform },
                { settings.host = it?.id?.toString() },
                PlatformCellRenderer()
            )
                .comment(message("settings.general.field.platform.help"))
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
        val current = platformComboBoxModel.selectedItem as? Platform

        platformComboBoxModel.removeAllElements()
        platformComboBoxModel.addAll(platforms.getAll())
        platformComboBoxModel.selectedItem = current?.let { platforms.getById(it.id) }
    }
}