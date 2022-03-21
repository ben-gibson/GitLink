package uk.co.ben_gibson.git.link

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import uk.co.ben_gibson.git.link.git.HostsProvider
import uk.co.ben_gibson.git.link.git.findRemote
import uk.co.ben_gibson.git.link.git.findRepositoryForProject
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification

class StartupActivity : StartupActivity.DumbAware {
    override fun runActivity(project: Project) {
        sendNotification(project, Notification.welcomeGuide())
        runInitialSetup(project)
    }

    private fun runInitialSetup(project: Project) {
//        RunOnceUtil.runOnceForProject(project, "GitLink.autoDetect") {
//        }
        val settings = project.service<ProjectSettings>()
        val hosts = project.service<HostsProvider>().provide()

        val repository = findRepositoryForProject(project) ?: return

        val host = repository.findRemote(settings.remote)?.let { hosts.forRemote(it) }

        if (host == null) {
            sendNotification(project, Notification.couldNotDetectGitHost(hosts.getById(settings.host)))
            return
        }

        sendNotification(project, Notification.remoteHostAutoDetected(host))

        settings.host = host.id.toString()
    }
}