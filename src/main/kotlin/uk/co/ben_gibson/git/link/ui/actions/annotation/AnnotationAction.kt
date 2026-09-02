package uk.co.ben_gibson.git.link.ui.actions.annotation

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.actions.ShowAnnotateOperationsPopup
import com.intellij.openapi.vfs.VirtualFile
import git4idea.annotate.GitFileAnnotation
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.ui.actions.Action

abstract class AnnotationAction(
    private val annotation: GitFileAnnotation,
    type: Type,
    private val createContext: (VirtualFile, Commit) -> Context,
) : Action(type) {
    override fun buildContext(project: Project, event: AnActionEvent): Context? {
        val lineNumber = ShowAnnotateOperationsPopup.getAnnotationLineNumber(event.dataContext)

        val revision = annotation.getLineRevisionNumber(lineNumber) ?: return null

        return createContext(annotation.file, Commit(revision.asString()))
    }
}
