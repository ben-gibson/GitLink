package uk.co.ben_gibson.git.link.ui.actions.menu

import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.*

class BrowserMenuAction : MenuAction("browser") {
    override fun handleAction(project: Project, context: ContextCurrentFile) {
        openInBrowser(project, context)
    }
}