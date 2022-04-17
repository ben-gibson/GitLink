package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.settings.ProjectSettings

@Service
class HostLocator(val project: Project) {
    fun locate() : Host? {
        val settings = project.service<ProjectSettings>()
        val hostsProvider = service<HostsProvider>()

        return settings.host?.let { hostsProvider.provide().getById(it) }
    }
}