package uk.co.ben_gibson.git.link.ui.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.copyToClipBoard
import uk.co.ben_gibson.git.link.openInBrowser
import uk.co.ben_gibson.git.link.platform.PlatformLocator

abstract class Action(private val type: Type): DumbAwareAction() {

    enum class Type(val key: String) {
        BROWSER("browser"),
        COPY("copy"),
        COPY_MARKDOWN("copy-markdown")
    }

    abstract fun buildContext(project: Project, event: AnActionEvent) : Context?

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        val context = buildContext(project, event) ?: return

        when(type) {
            Type.BROWSER -> openInBrowser(project, context)
            Type.COPY -> copyToClipBoard(project, context)
            Type.COPY_MARKDOWN -> copyToClipBoard(project, context, true)
        }
    }

    open fun shouldBeEnabled(event: AnActionEvent) = true

    override fun update(event: AnActionEvent) {
        super.update(event)

        event.presentation.isEnabled = event.project != null

        val project = event.project ?: return

        val host = project.service<PlatformLocator>().locate()

        if (host == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        event.presentation.isEnabled = shouldBeEnabled(event)

        event.presentation.icon = host.icon
        event.presentation.text = GitLinkBundle.message("actions.${type.key}.title", host.name)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}