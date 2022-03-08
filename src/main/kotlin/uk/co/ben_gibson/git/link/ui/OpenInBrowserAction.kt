package uk.co.ben_gibson.git.link.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.GitLinkBundle
import uk.co.ben_gibson.git.link.Settings
import uk.co.ben_gibson.git.link.openInBrowser
import uk.co.ben_gibson.git.link.url.Context
import uk.co.ben_gibson.git.link.url.LineSelection

class OpenInBrowserAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val lineSelection = getLineSelection(project)

        openInBrowser(project, Context(file, lineSelection))
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

    private fun getLineSelection(project: Project): LineSelection? {
        val editor: Editor? = FileEditorManager.getInstance(project).selectedTextEditor

        val primaryCaret = editor?.caretModel?.primaryCaret ?: return null

        val start = primaryCaret.selectionStart
        val end = primaryCaret.selectionEnd

        return LineSelection(start, end)
    }
}