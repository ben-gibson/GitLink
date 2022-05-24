package uk.co.ben_gibson.git.link.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification

class ApplicationStartupListener : StartupActivity.DumbAware {
    override fun runActivity(project: Project) {
        val settings = service<ApplicationSettings>()
        val version = GitLinkBundle.plugin()?.version

        if (version != settings.lastVersion) {
            settings.lastVersion = version
            sendNotification(Notification.welcome(version ?: "Unknown"), project)
        }
    }
}