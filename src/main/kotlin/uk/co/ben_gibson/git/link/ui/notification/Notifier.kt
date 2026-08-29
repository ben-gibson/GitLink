package uk.co.ben_gibson.git.link.ui.notification

import com.intellij.openapi.project.Project

interface Notifier {
    fun send(notification: Notification, project: Project? = null)
}
