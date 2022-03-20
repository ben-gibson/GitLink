package uk.co.ben_gibson.git.link.ui.actions.annotation

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.annotate.UpToDateLineNumberListener
import git4idea.annotate.GitFileAnnotation
import uk.co.ben_gibson.git.link.ContextFileAtCommit
import uk.co.ben_gibson.git.link.copyToClipBoard
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.ui.actions.Action

class FileClipboardAction(private val annotation: GitFileAnnotation):
    Action(key = "clipboard"),
    UpToDateLineNumberListener
{
    private var lineNumber = -1

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val revision = annotation.getLineRevisionNumber(lineNumber) ?: return

        copyToClipBoard(project, ContextFileAtCommit(annotation.file, Commit(revision.toString())))
    }

    override fun consume(integer: Int) {
        lineNumber = integer
    }
}