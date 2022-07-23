package uk.co.ben_gibson.git.link.platform

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import git4idea.repo.GitRepositoryManager
import uk.co.ben_gibson.git.link.git.domain
import uk.co.ben_gibson.git.link.git.locateRemote
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ProjectSettings

@Service
class PlatformDetector(val project: Project) {
    fun detect() : Platform? {
        val applicationSettings = service<ApplicationSettings>()
        val settings = project.service<ProjectSettings>()

        val platforms = project.service<PlatformRepository>()

        val projectDirectory = project.guessProjectDir() ?: return null

        val repository = GitRepositoryManager.getInstance(project).getRepositoryForFile(projectDirectory) ?: return null

        val remote = repository.locateRemote(settings.remote) ?: return null

        return remote.domain?.let {
            platforms.getByDomain(it) ?: applicationSettings.findPlatformIdByCustomDomain(it)?.let { id -> platforms.getById(id) }
        }
    }
}