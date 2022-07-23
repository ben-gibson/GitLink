package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.project.Project
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.platform.Platform

class Pass(val project: Project, val context: Context) {
    var platform: Platform? = null
    var repository: GitRepository? = null
    var remote: GitRemote? = null

    fun platformOrThrow() = platform ?: throw IllegalStateException("Platform not set")
    fun repositoryOrThrow() = repository ?: throw IllegalStateException("Repository not set")
    fun remoteOrThrow() = remote ?: throw IllegalStateException("Remote not set")
}