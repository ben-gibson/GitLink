package uk.co.ben_gibson.git.link.ui.actions.vcslog

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.vcs.log.VcsLogDataKeys.VCS_LOG_COMMIT_SELECTION
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.ContextCommit
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.ui.actions.Action

class BrowserAction: Action(Type.BROWSER) {
    override fun buildContext(project: Project, event: AnActionEvent) : Context? {
        val vcsCommit = event.getData(VCS_LOG_COMMIT_SELECTION)?.cachedFullDetails?.get(0) ?: return null

        return ContextCommit(vcsCommit.root, Commit(vcsCommit.id.asString()))
    }

    override fun shouldBeEnabled(event: AnActionEvent): Boolean {
        return event.getData(VCS_LOG_COMMIT_SELECTION)?.cachedFullDetails?.size == 1
    }
}