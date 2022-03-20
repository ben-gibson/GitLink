package uk.co.ben_gibson.git.link.ui.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.*
import uk.co.ben_gibson.git.link.git.HostsProvider

abstract class Action(private val type: Type): AnAction() {

    enum class Type(val key: String) {
        BROWSER("browser"),
        CLIPBOARD("clipboard")
    }

    abstract fun buildContext(project: Project, event: AnActionEvent) : Context?

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        val context = buildContext(project, event) ?: return

        when(type) {
            Type.BROWSER -> openInBrowser(project, context)
            Type.CLIPBOARD -> copyToClipBoard(project, context)
        }
    }

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
            event.presentation.text = GitLinkBundle.message("actions.${type.key}.title", host.displayName)
        }
    }
}