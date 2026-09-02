package uk.co.ben_gibson.git.link.ui.extensions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.vcs.annotate.FileAnnotation
import git4idea.annotate.GitFileAnnotation
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.ui.actions.annotation.*
import com.intellij.openapi.vcs.annotate.AnnotationGutterActionProvider as IntellijAnnotationGutterActionProvider

class AnnotationGutterActionProvider : IntellijAnnotationGutterActionProvider {
    override fun createAction(annotation: FileAnnotation): AnAction {
        return FileAndCommitGroup(annotation)
    }

    private class FileAndCommitGroup(annotation: FileAnnotation): ActionGroup(message("name"), true) {
        private val children: Array<AnAction> = when (annotation) {
            is GitFileAnnotation -> arrayOf(
                DefaultActionGroup(
                    message("actions.annotation.file.title"),
                    listOf(FileBrowserAction(annotation), FileCopyAction(annotation), FileMarkdownAction(annotation))
                ).apply {
                    isPopup = true
                },
                DefaultActionGroup(
                    message("actions.annotation.commit.title"),
                    listOf(CommitBrowserAction(annotation), CommitCopyAction(annotation), CommitMarkdownAction(annotation))
                ).apply {
                    isPopup = true
                }
            )
            else -> arrayOf()
        }

        override fun getChildren(e: AnActionEvent?): Array<AnAction> = children
    }
}