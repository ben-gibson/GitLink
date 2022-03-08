package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vcs.VcsException
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

fun GitRepository.findRemote(name: String) : GitRemote? {
    return GitUtil.findRemoteByName(this, name)
}

fun GitRepository.headCommit() : Commit? {
    val revision = currentRevision ?: return null

    return Commit(revision)
}