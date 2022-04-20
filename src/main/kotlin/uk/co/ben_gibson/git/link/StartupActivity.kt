package uk.co.ben_gibson.git.link

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import uk.co.ben_gibson.git.link.git.HostsProvider
import uk.co.ben_gibson.git.link.git.findRemote
import uk.co.ben_gibson.git.link.git.findRepositoryForProject
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification

class StartupActivity : StartupActivity.DumbAware {
    override fun runActivity(project: Project) {
        val settings = service<ApplicationSettings>()
        val version = GitLinkBundle.plugin()?.version

        if (version != settings.lastVersion) {
            settings.lastVersion = version
            sendNotification(Notification.welcome(version ?: "Unknown"), project)
        }

        runInitialSetup(project)
    }

    private fun runInitialSetup(project: Project) {
        val settings = project.service<ProjectSettings>()

        if (settings.host != null) {
            return
        }

        val hosts = project.service<HostsProvider>().provide()

        val repository = findRepositoryForProject(project) ?: return

        val host = repository.findRemote(settings.remote)?.let { hosts.forRemote(it) }

        if (host == null) {
            sendNotification(Notification.couldNotDetectGitHost(project), project)
            return
        }

        sendNotification(Notification.remoteHostAutoDetected(host, project), project)

        settings.host = host.id.toString()
    }
}