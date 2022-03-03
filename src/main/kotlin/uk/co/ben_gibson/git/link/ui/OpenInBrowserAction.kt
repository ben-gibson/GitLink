package uk.co.ben_gibson.git.link.ui

import com.intellij.ide.actions.ShowSettingsUtilImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressManager
import uk.co.ben_gibson.git.link.Settings
import uk.co.ben_gibson.git.link.processGitLink
import uk.co.ben_gibson.git.link.git.RemoteHost

class OpenInBrowserAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val progress = ProgressManager.getInstance().progressIndicator

        ProgressManager.getInstance().runProcess(::processGitLink, progress)
        ShowSettingsUtilImpl.showSettingsDialog(e.project, "GitLink.Settings", null)
    }

    override fun update(e: AnActionEvent) {
        super.update(e)

        e.presentation.isEnabled = e.project != null

        val settings = e.project?.service<Settings>()

        settings.let { e.presentation.icon = RemoteHost.GITLAB.icon }
    }
}