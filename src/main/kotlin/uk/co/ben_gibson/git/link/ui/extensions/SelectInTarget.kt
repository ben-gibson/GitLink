package uk.co.ben_gibson.git.link.ui.extensions

import com.intellij.ide.SelectInContext
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.ContextCurrentFile
import uk.co.ben_gibson.git.link.platform.PlatformLocator
import uk.co.ben_gibson.git.link.openInBrowser
import com.intellij.ide.SelectInTarget as IntellijSelectInTarget

class SelectInTarget(private val project: Project) : IntellijSelectInTarget {
    override fun canSelect(context: SelectInContext) = project.service<PlatformLocator>().locate() != null

    override fun selectIn(context: SelectInContext, requestFocus: Boolean) {
        openInBrowser(context.project, ContextCurrentFile(context.virtualFile))
    }

    override fun toString(): String {
        val platform = project.service<PlatformLocator>().locate()

        return platform?.name ?: "Gitlink"
    }
}