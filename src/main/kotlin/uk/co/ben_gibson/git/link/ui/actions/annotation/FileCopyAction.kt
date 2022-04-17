package uk.co.ben_gibson.git.link.ui.actions.annotation

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.annotate.UpToDateLineNumberListener
import git4idea.annotate.GitFileAnnotation
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.ContextFileAtCommit
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.ui.actions.Action

class FileCopyAction(private val annotation: GitFileAnnotation):
    Action(Type.COPY),
    UpToDateLineNumberListener
{
    private var lineNumber = -1

    override fun buildContext(project: Project, event: AnActionEvent): Context? {
        val revision = annotation.getLineRevisionNumber(lineNumber) ?: return null

        return ContextFileAtCommit(annotation.file, Commit(revision.toString()))
    }

    override fun consume(integer: Int) {
        lineNumber = integer
    }
}