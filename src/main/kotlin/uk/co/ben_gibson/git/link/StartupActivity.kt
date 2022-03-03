package uk.co.ben_gibson.git.link

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification

class StartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        sendNotification(project, Notification.welcomeGuide())
    }
}