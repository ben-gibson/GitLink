package uk.co.ben_gibson.git.link.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import uk.co.ben_gibson.git.link.git.RemoteHost

class OpenInBrowserAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

    }

    override fun update(e: AnActionEvent) {
        super.update(e)

        e.presentation.icon = RemoteHost.GITLAB.icon()
    }
}