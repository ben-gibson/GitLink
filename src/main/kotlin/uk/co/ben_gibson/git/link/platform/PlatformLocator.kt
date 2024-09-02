package uk.co.ben_gibson.git.link.platform

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.settings.ProjectSettings

@Service(Service.Level.PROJECT)
class PlatformLocator(val project: Project) {
    fun locate() : Platform? {
        val settings = project.service<ProjectSettings>()

        val platformId = settings.host?: return null

        val platforms = service<PlatformRepository>()

        return platforms.getById(platformId)
    }
}