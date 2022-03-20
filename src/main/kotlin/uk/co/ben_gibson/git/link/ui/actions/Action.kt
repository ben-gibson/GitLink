package uk.co.ben_gibson.git.link.ui.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.Settings
import uk.co.ben_gibson.git.link.git.HostsProvider

abstract class Action(private val key: String): AnAction() {
    open fun shouldBeEnabled(event: AnActionEvent) = true

    override fun update(event: AnActionEvent) {
        super.update(event)

        event.presentation.isEnabled = event.project != null && shouldBeEnabled(event)

        val project = event.project ?: return

        val settings = project.service<Settings>()
        val hosts = project.service<HostsProvider>().provide()
        val host = hosts.getById(settings.host)

        settings.let {
            event.presentation.icon = host.icon
            event.presentation.text = GitLinkBundle.message("actions.$key.title", host.displayName)
        }
    }
}