package uk.co.ben_gibson.git.link

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.pipeline.Pipeline
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.Notifier
import uk.co.ben_gibson.url.URL
import java.awt.datatransfer.StringSelection

fun openInBrowser(project: Project, context: Context) {
    processGitLink(project, context) { BrowserUtil.browse(it.toString()) }
}

fun copyToClipBoard(project: Project, context: Context, asMarkdown: Boolean = false) {
    processGitLink(project, context) {
        val url = if (asMarkdown) {
            val label = when(context) {
                is Context.Commit -> context.commit.shortHash
                is Context.File -> context.file.name
                is Context.FileAtCommit -> context.file.name
            }

            "[${label}](${it})"
        } else {
            it.toString()
        }

        CopyPasteManager.getInstance().setContents(StringSelection(url))

        service<Notifier>().send(Notification.linkCopied(it), project)
    }
}

private fun processGitLink(project: Project, context: Context, handle: (URL) -> Unit) {
    runBackgroundableTask(message("name"), project, false) {
        val pipeline = project.service<Pipeline>()

        pipeline.accept(context)?.let(handle)
    }
}