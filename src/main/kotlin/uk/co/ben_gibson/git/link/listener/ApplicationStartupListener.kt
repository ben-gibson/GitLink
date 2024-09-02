package uk.co.ben_gibson.git.link.listener

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.platform.PlatformDetector
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification

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
        sendNotification(Notification.welcome(version ?: "Unknown"), project)
    }

    private fun detectPlatform(project: Project) {
        val projectSettings = project.service<ProjectSettings>()

        if (projectSettings.host != null) {
            return
        }

        project.service<PlatformDetector>().detect { platform ->
            if (platform == null) {
                sendNotification(Notification.couldNotDetectPlatform(project), project)
                return@detect
            }

            sendNotification(Notification.platformAutoDetected(platform, project), project)

            projectSettings.host = platform.id.toString()
        }
    }
}
