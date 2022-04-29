package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.project.Project
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.git.Host

class Pass(val project: Project, val context: Context) {
    var host: Host? = null
    var repository: GitRepository? = null
    var remote: GitRemote? = null

    fun hostOrThrow() = host ?: throw IllegalStateException("Host not set")
    fun repositoryOrThrow() = repository ?: throw IllegalStateException("Repository not set")
    fun remoteOrThrow() = remote ?: throw IllegalStateException("Remote not set")
}