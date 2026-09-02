package uk.co.ben_gibson.git.link.ui.actions.menu

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.*
import uk.co.ben_gibson.git.link.git.LineSelection
import uk.co.ben_gibson.git.link.ui.actions.Action
import uk.co.ben_gibson.git.link.editor.lineSelection

abstract class MenuAction(type: Type) : Action(type) {
    override fun buildContext(project: Project, event: AnActionEvent): Context? {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return null

        return Context.File(file, resolveLineSelection(event))
    }

    private fun resolveLineSelection(event: AnActionEvent): LineSelection? {
        val editor = event.getData(CommonDataKeys.EDITOR) ?: return null

        // Honour an explicit selection anywhere, but only fall back to the caret line when the action came
        // from the editor itself, not from the main menu, a project view entry or an editor tab
        if (!editor.selectionModel.hasSelection() && event.place in WHOLE_FILE_PLACES) {
            return null
        }

        return editor.lineSelection
    }

    companion object {
        private val WHOLE_FILE_PLACES = setOf(
            ActionPlaces.MAIN_MENU,
            ActionPlaces.PROJECT_VIEW_POPUP,
            ActionPlaces.EDITOR_TAB_POPUP,
        )
    }
}
