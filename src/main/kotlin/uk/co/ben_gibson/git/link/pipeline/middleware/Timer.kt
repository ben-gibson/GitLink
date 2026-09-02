package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.pipeline.Pass
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.Notifier
import uk.co.ben_gibson.url.URL
import java.time.InstantSource

@Service
class Timer(private val clock: InstantSource = InstantSource.system()) : Middleware {
    override fun invoke(pass: Pass, next: () -> URL?) : URL? {
        val settings = pass.project.service<ProjectSettings>()

        if (!settings.shouldCheckRemote || !settings.showPerformanceTip) {
            return next()
        }

        val startTime = clock.millis()

        val url = next()

        val total = clock.millis() - startTime

        if (total > 1000) {
            service<Notifier>().send(Notification.performanceTips(), pass.project)
        }

        return url
    }
}

