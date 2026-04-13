package uk.co.ben_gibson.git.link.pipeline

import com.intellij.openapi.project.Project
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.platform.Platform

class Pass(
    val project: Project,
    val context: Context,
    val platform: Platform,
    val repository: GitRepository,
    val remote: GitRemote
)