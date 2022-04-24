package uk.co.ben_gibson.git.link.git

import git4idea.GitUtil
import git4idea.repo.GitRepository

fun GitRepository.locateRemote(name: String) = GitUtil.findRemoteByName(this, name)
fun GitRepository.currentCommit() = currentRevision?.let { Commit(it) }