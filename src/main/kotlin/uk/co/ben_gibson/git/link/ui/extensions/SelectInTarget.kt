package uk.co.ben_gibson.git.link.ui.extensions

import com.intellij.ide.SelectInContext
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.ContextCurrentFile
import uk.co.ben_gibson.git.link.git.HostLocator
import uk.co.ben_gibson.git.link.openInBrowser
import com.intellij.ide.SelectInTarget as IntellijSelectInTarget

class SelectInTarget(private val project: Project) : IntellijSelectInTarget {
    override fun canSelect(context: SelectInContext) = project.service<HostLocator>().locate() != null

    override fun selectIn(context: SelectInContext, requestFocus: Boolean) {
        openInBrowser(context.project, ContextCurrentFile(context.virtualFile))
    }

    override fun toString(): String {
        val host = project.service<HostLocator>().locate()

        return host?.displayName ?: "Gitlink"
    }
}