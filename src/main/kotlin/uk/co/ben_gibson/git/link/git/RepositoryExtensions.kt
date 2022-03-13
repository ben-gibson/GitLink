package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.vfs.VirtualFile
import git4idea.GitUtil
import git4idea.repo.GitRepository

fun GitRepository.findRemote(name: String) = GitUtil.findRemoteByName(this, name)
fun GitRepository.currentCommit() = currentRevision?.let { Commit(it) }
fun GitRepository.getRelativeFilePath(file: VirtualFile) = file.path.substring(root.path.length)