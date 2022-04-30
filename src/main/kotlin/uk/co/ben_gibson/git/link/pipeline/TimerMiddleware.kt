package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification
import java.net.URL

@Service
class TimerMiddleware : Middleware {
    override val priority = 40

    override fun invoke(pass: Pass, next: () -> URL?) : URL? {
        val settings = pass.project.service<ProjectSettings>()

        if (!settings.checkCommitOnRemote || !settings.showPerformanceTip) {
            return next()
        }

        val startTime = System.currentTimeMillis()

        val url = next()

        val total = System.currentTimeMillis() - startTime;

        if (total > 1000) {
            sendNotification(Notification.performanceTips(pass.project), pass.project)
        }

        return url;
    }
}

