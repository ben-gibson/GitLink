package uk.co.ben_gibson.git.link

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import uk.co.ben_gibson.git.link.git.findRepositoryForProject
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
        val settings = project.service<Settings>()

        val repository = findRepositoryForProject(project) ?: return

        val guessedRemoteHost = repository.guessRemoteHost(settings.remote)

        if (guessedRemoteHost == null) {
            sendNotification(project, Notification.couldNotAutoDetectRemoteHost(settings.remoteHost))
            return
        }

        sendNotification(project, Notification.remoteHostAutoDetected(guessedRemoteHost))

        settings.remoteHost = guessedRemoteHost
    }
}