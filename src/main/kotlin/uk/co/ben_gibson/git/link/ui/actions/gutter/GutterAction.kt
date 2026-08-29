package uk.co.ben_gibson.git.link.ui.actions.gutter

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.git.LineSelection
import uk.co.ben_gibson.git.link.ui.actions.Action

abstract class GutterAction(type: Type) : Action(type) {
    override fun buildContext(project: Project, event: AnActionEvent): Context? {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return null
        val line = event.getData(EditorGutterComponentEx.LOGICAL_LINE_AT_CURSOR)

        return Context.File(file, line?.plus(1)?.let { LineSelection(it, it) })
    }
}