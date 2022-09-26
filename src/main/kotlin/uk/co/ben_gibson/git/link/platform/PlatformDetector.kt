package uk.co.ben_gibson.git.link.platform

import com.intellij.dvcs.repo.VcsRepositoryManager
import com.intellij.dvcs.repo.VcsRepositoryMappingListener
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager
import uk.co.ben_gibson.git.link.git.domain
import uk.co.ben_gibson.git.link.git.locateRemote
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ProjectSettings

@Service
class PlatformDetector(val project: Project) {
    fun detect(consumer: (Platform?) -> Unit) {
        val projectDirectory = project.guessProjectDir()
        if (projectDirectory == null) {
           consumer.invoke(null)
           return
        }

        getRepositoryForFile(projectDirectory) { repository -> getPlatformForRepository(repository).let(consumer) }
    }

    private fun getPlatformForRepository(repository: GitRepository?): Platform? {
        repository ?: return null

        val settings = project.service<ProjectSettings>()

        val remote = repository.locateRemote(settings.remote) ?: return null

        val applicationSettings = service<ApplicationSettings>()
        val platforms = project.service<PlatformRepository>()

        return remote.domain?.let {
            platforms.getByDomain(it) ?: applicationSettings.findPlatformIdByCustomDomain(it)?.let { id -> platforms.getById(id) }
        }
    }

    private fun getRepositoryForFile(projectDirectory: VirtualFile, consumer: (GitRepository?) -> Unit) {
        val gitRepositoryManager = GitRepositoryManager.getInstance(project)
        val repository = gitRepositoryManager.getRepositoryForFile(projectDirectory)
        if (repository != null) {
            consumer.invoke(repository)
            return
        }

        val busConnection = project.messageBus.connect()
        busConnection
            .subscribe(
                VcsRepositoryManager.VCS_REPOSITORY_MAPPING_UPDATED,
                VcsRepositoryMappingListener {
                    busConnection.disconnect()
                    gitRepositoryManager.getRepositoryForFile(projectDirectory).let(consumer)
                }
            )
    }

}
