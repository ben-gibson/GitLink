package uk.co.ben_gibson.git.link.ui.extensions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.vcs.annotate.FileAnnotation
import com.intellij.openapi.vcs.annotate.UpToDateLineNumberListener
import git4idea.annotate.GitFileAnnotation
import uk.co.ben_gibson.git.link.ui.actions.annotation.CommitBrowserAction
import uk.co.ben_gibson.git.link.ui.actions.annotation.CommitCopyAction
import uk.co.ben_gibson.git.link.ui.actions.annotation.FileBrowserAction
import uk.co.ben_gibson.git.link.ui.actions.annotation.FileCopyAction
import com.intellij.openapi.vcs.annotate.AnnotationGutterActionProvider as IntellijAnnotationGutterActionProvider

class AnnotationGutterActionProvider : IntellijAnnotationGutterActionProvider {
    override fun createAction(annotation: FileAnnotation): AnAction {
        return FileAndCommitGroup(annotation)
    }

    private class FileAndCommitGroup(val annotation: FileAnnotation): ActionGroup("GitLink", true), UpToDateLineNumberListener {
        private val children: Array<AnAction> = when (annotation) {
            is GitFileAnnotation -> arrayOf(
                DefaultActionGroup(
                    "File",
                    listOf(FileBrowserAction(annotation), FileCopyAction(annotation))
                ).apply {
                    isPopup = true
                },
                DefaultActionGroup(
                    "Commit",
                    listOf(CommitBrowserAction(annotation), CommitCopyAction(annotation))
                ).apply {
                    isPopup = true
                }
            )
            else -> arrayOf()
        }

        override fun getChildren(e: AnActionEvent?): Array<AnAction> = children

        override fun consume(lineNumber: Int) {
            lineListenerChildActions().forEach { it.consume(lineNumber) }
        }

        private fun lineListenerChildActions() = findLeafActions(children.toList())
            .filterIsInstance<UpToDateLineNumberListener>()

        private fun findLeafActions(actions: List<AnAction>) : List<AnAction> {
            val leafs = mutableListOf<AnAction>()

            actions.forEach {
                when (it) {
                    is ActionGroup -> leafs.addAll(findLeafActions(it.getChildren(null).toList()))
                    else -> leafs.add(it)
                }
            }

            return leafs
        }
    }
}