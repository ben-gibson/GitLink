package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vcs.VcsException
import com.intellij.openapi.vfs.VirtualFile
import git4idea.GitUtil
import git4idea.repo.GitRepository

fun findRepositoryForProject(project: Project) = project.guessProjectDir()?.let { findRepositoryForFile(project, it) }

fun findRepositoryForFile(project: Project, file: VirtualFile) : GitRepository? {
    return try {
        GitUtil.getRepositoryForFile(project, file)
    } catch (exception : VcsException) {
        null
    }
}