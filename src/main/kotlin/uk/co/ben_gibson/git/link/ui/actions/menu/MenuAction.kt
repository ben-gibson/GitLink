package uk.co.ben_gibson.git.link.ui.actions.menu

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.*
import uk.co.ben_gibson.git.link.ui.actions.Action
import uk.co.ben_gibson.git.link.editor.lineSelection

abstract class MenuAction(type: Type) : Action(type) {
    override fun buildContext(project: Project, event: AnActionEvent): Context? {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return null

        val editor = FileEditorManager.getInstance(project).selectedTextEditor

        return Context.File(file, editor?.lineSelection)
    }
}