package uk.co.ben_gibson.git.link.listener

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.platform.PlatformDetector
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.Notifier

class ApplicationStartupListener : ProjectActivity {
    override suspend fun execute(project: Project) {
        showVersionNotification(project)
        detectPlatform(project)
    }

    private fun showVersionNotification(project: Project) {
        val settings = service<ApplicationSettings>()
        val version = GitLinkBundle.plugin()?.version

        if (version == settings.lastVersion) {
            return
        }

        settings.lastVersion = version
        service<Notifier>().send(Notification.welcome(version ?: "Unknown"), project)
    }

    private fun detectPlatform(project: Project) {
        val projectSettings = project.service<ProjectSettings>()

        if (projectSettings.platformId != null) {
            return
        }

        project.service<PlatformDetector>().detect { platform ->
            if (platform == null) {
                service<Notifier>().send(Notification.couldNotDetectPlatform(), project)
                return@detect
            }

            service<Notifier>().send(Notification.platformAutoDetected(platform), project)

            projectSettings.platformId = platform.id.toString()
        }
    }
}
