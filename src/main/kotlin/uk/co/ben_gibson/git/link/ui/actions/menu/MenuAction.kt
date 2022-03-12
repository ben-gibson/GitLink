package uk.co.ben_gibson.git.link.ui.actions.menu

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.*
import uk.co.ben_gibson.git.link.ui.lineSelection

abstract class MenuAction(private val key: String) : AnAction() {

    abstract fun handleAction(project: Project, context: ContextCurrentFile)

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val editor: Editor? = FileEditorManager.getInstance(project).selectedTextEditor
        val lineSelection = editor?.lineSelection

        handleAction(project, ContextCurrentFile(file, lineSelection))
        //ShowSettingsUtilImpl.showSettingsDialog(event.project, "GitLink.Settings", null)
    }

    override fun update(event: AnActionEvent) {
        super.update(event)

        event.presentation.isEnabled = event.project != null

        val project = event.project ?: return

        val settings = project.service<Settings>()
        val remoteHost = settings.host

        settings.let {
            event.presentation.icon = remoteHost.icon
            event.presentation.text = GitLinkBundle.message("actions.$key.title", remoteHost.displayName)
        }
    }
}