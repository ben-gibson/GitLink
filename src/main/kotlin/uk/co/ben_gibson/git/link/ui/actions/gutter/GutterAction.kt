package uk.co.ben_gibson.git.link.ui.actions.gutter

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorGutter
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.ContextCurrentFile
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.ui.actions.Action

abstract class GutterAction(type: Type) : Action(type) {
    override fun buildContext(project: Project, event: AnActionEvent): Context? {
        val gutter = event.getData(EditorGutter.KEY) ?: return null
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return null

        val line = event.getData(EditorGutterComponentEx.LOGICAL_LINE_AT_CURSOR)

        return ContextCurrentFile(file, line?.plus(1)?.let { LineSelection(it, it) })
    }
}