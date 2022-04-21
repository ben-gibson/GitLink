package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification
import java.net.URL
import uk.co.ben_gibson.git.link.Context

@Service
class TimerMiddleware : Middleware {
    override val priority = 4

    override fun invoke(project: Project, context: Context, next: () -> URL?) : URL? {
        val startTime = System.currentTimeMillis()

        val url = next()

        val total = System.currentTimeMillis() - startTime;

        if (total > 1000 && project.service<ProjectSettings>().checkCommitOnRemote) {
            sendNotification(Notification.performanceTips(project), project)
        }

        return url;
    }
}

