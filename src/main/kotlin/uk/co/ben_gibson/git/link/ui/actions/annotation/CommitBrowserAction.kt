package uk.co.ben_gibson.git.link.ui.actions.annotation

import com.intellij.openapi.actionSystem.AnActionEvent
import git4idea.annotate.GitFileAnnotation
import uk.co.ben_gibson.git.link.ui.actions.Action
import com.intellij.openapi.vcs.annotate.UpToDateLineNumberListener;
import uk.co.ben_gibson.git.link.ContextCommit
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.openInBrowser

class CommitBrowserAction(private val annotation: GitFileAnnotation):
    Action(key = "browser"),
    UpToDateLineNumberListener
{
    private var lineNumber = -1

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val revision = annotation.getLineRevisionNumber(lineNumber) ?: return

        openInBrowser(project, ContextCommit(annotation.file, Commit(revision.toString())))
    }

    override fun consume(number: Int) {
        lineNumber = number
    }
}