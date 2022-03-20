package uk.co.ben_gibson.git.link.ui.actions.menu

import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.*

class CopyAction : MenuAction("clipboard") {
    override fun handleAction(project: Project, context: ContextCurrentFile) {
        copyToClipBoard(project, context)
    }
}