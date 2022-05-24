package uk.co.ben_gibson.git.link.listeners

import com.intellij.dvcs.repo.VcsRepositoryMappingListener
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import git4idea.repo.GitRepositoryManager
import uk.co.ben_gibson.git.link.git.*
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ProjectSettings
import uk.co.ben_gibson.git.link.ui.notification.Notification
import uk.co.ben_gibson.git.link.ui.notification.sendNotification

class VcsMappingChangedListener(val project: Project) : VcsRepositoryMappingListener {

    override fun mappingChanged() {
        val settings = project.service<ProjectSettings>()
        val applicationSettings = service<ApplicationSettings>()

        if (settings.host != null) {
            return
        }

        val hosts = project.service<HostsProvider>().provide()

        val projectDirectory = project.guessProjectDir() ?: return

        val repository = GitRepositoryManager.getInstance(project)
            .getRepositoryForFileQuick(projectDirectory) ?: return

        val remote = repository.locateRemote(settings.remote) ?: return

        val host = remote.domain?.let {
            hosts.getByHostDomain(it) ?: applicationSettings.findHostIdByCustomDomain(it)
                ?.let { id -> hosts.getById(id) }
        }

        if (host == null) {
            sendNotification(Notification.couldNotDetectGitHost(project), project)
            return
        }

        sendNotification(Notification.remoteHostAutoDetected(host, project), project)

        settings.host = host.id.toString()
    }
}