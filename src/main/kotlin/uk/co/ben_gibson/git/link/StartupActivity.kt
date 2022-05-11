package uk.co.ben_gibson.git.link

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import uk.co.ben_gibson.git.link.git.*
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
        val projectSettings = project.service<ProjectSettings>()
        val applicationSettings = service<ApplicationSettings>()

        if (projectSettings.host != null) {
            return
        }

        val hosts = project.service<HostsProvider>().provide()

        val repository = findRepositoryForProject(project) ?: return
        val remote = repository.locateRemote(projectSettings.remote) ?: return

        val host = remote.domain?.let {
            hosts.getByHostDomain(it) ?: applicationSettings.findHostIdByCustomDomain(it)?.let { id -> hosts.getById(id) }
        }

        if (host == null) {
            sendNotification(Notification.couldNotDetectGitHost(project), project)
            return
        }

        sendNotification(Notification.remoteHostAutoDetected(host, project), project)

        projectSettings.host = host.id.toString()
    }
}