package uk.co.ben_gibson.git.link.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import uk.co.ben_gibson.git.link.*

class OpenInBrowserAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val editor: Editor? = FileEditorManager.getInstance(project).selectedTextEditor
        val lineSelection = editor?.lineSelection

        openInBrowser(project, ContextCurrentFile(file, lineSelection))
        //ShowSettingsUtilImpl.showSettingsDialog(event.project, "GitLink.Settings", null)
    }

    override fun update(event: AnActionEvent) {
        super.update(event)

        event.presentation.isEnabled = event.project != null

        val project = event.project ?: return

        val settings = project.service<Settings>()
        val remoteHost = settings.remoteHost

        settings.let {
            event.presentation.icon = remoteHost.icon
            event.presentation.text = GitLinkBundle.message("actions.menu.browser", remoteHost.displayName)
        }
    }
}