package uk.co.ben_gibson.git.link.ui.actions.vcslog

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.vcs.log.VcsLogDataKeys
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.ContextCommit
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.ui.actions.Action

class CopyAction: Action(Type.COPY) {
    override fun buildContext(project: Project, event: AnActionEvent): Context? {
        val vcsLog = event.getData(VcsLogDataKeys.VCS_LOG) ?: return null
        val vcsCommit = vcsLog.selectedDetails[0]

        return ContextCommit(vcsCommit.root, Commit(vcsCommit.id.toShortString()))
    }

    override fun shouldBeEnabled(event: AnActionEvent): Boolean {
        val log = event.getData(VcsLogDataKeys.VCS_LOG) ?: return false

        return log.selectedDetails.size == 1
    }
}