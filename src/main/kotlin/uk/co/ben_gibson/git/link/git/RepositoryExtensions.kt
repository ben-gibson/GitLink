package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.vfs.VirtualFile
import git4idea.GitLocalBranch
import git4idea.GitUtil
import git4idea.commands.Git
import git4idea.commands.GitCommand
import git4idea.commands.GitCommandResult
import git4idea.commands.GitLineHandler
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository

fun GitRepository.isBranchOnRemote(remote: GitRemote, branch: GitLocalBranch) : Boolean {
    val result = Git.getInstance().lsRemote(
        project,
        root,
        remote,
        remote.firstUrl,
        branch.fullName,
        "--heads"
    )

    repositoryFiles

    return (result.success() && result.output.size == 1) || branch.findTrackedBranch(this) != null
}

fun GitRepository.isCommitOnRemote(remote: GitRemote, commit: Commit): Boolean {
    val command = GitLineHandler(project, root, GitCommand.BRANCH)

    command.addParameters("-r", "--contains", commit.toString())

    val result: GitCommandResult = Git.getInstance().runCommand(command)

    if (!result.success()) {
        return false
    }

    return result.output.find{ it.trim().startsWith(remote.name) } != null
}

fun GitRepository.findRemote(name: String) = GitUtil.findRemoteByName(this, name)

fun GitRepository.currentCommit() = currentRevision?.let { Commit(it) }

fun GitRepository.guessRemoteHost(remoteName: String) = findRemote(remoteName)?.let { Host.forRemote(it) }

fun GitRepository.getRelativeFilePath(file: VirtualFile) = file.path.substring(root.path.length)

fun GitRepository.currentBranch(default: String) = currentBranch?.name ?: default