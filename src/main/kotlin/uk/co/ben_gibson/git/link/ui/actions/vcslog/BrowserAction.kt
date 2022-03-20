package uk.co.ben_gibson.git.link.ui.actions.vcslog

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.vcs.log.VcsLogDataKeys
import uk.co.ben_gibson.git.link.ContextCommit
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.openInBrowser
import uk.co.ben_gibson.git.link.ui.actions.Action

class BrowserAction: Action(key="browser") {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val vcsLog = event.getData(VcsLogDataKeys.VCS_LOG) ?: return

        val vcsCommit = vcsLog.selectedDetails[0]

        openInBrowser(project, ContextCommit(vcsCommit.root, Commit(vcsCommit.id.toShortString())))
    }

    override fun shouldBeEnabled(event: AnActionEvent): Boolean {
        val log = event.getData(VcsLogDataKeys.VCS_LOG) ?: return false

        return log.selectedDetails.size == 1
    }
}