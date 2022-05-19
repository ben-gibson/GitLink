package uk.co.ben_gibson.git.link.ui.layout

import com.intellij.ui.layout.Cell
import com.intellij.ui.layout.CellBuilder
import uk.co.ben_gibson.git.link.GitLinkBundle
import javax.swing.JComponent

fun Cell.reportBugLink(): CellBuilder<JComponent> {
    return browserLink(GitLinkBundle.message("actions.report-bug.title"), GitLinkBundle.URL_BUG_REPORT.toString())
}