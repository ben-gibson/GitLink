package uk.co.ben_gibson.git.link.git

import git4idea.GitLocalBranch
import git4idea.commands.Git
import git4idea.commands.GitCommand
import git4idea.commands.GitCommandResult
import git4idea.commands.GitLineHandler
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import uk.co.ben_gibson.url.Host
import uk.co.ben_gibson.url.URL
import java.net.URI

val GitRemote.domain : Host? get() = httpUrl?.host

val GitRemote.httpUrl : URL? get() {
    var url = firstUrl ?: return null

    url = url.trim()

    // Azure expects the .git postfix on the repo name unlike everything else
    if (!url.contains("dev.azure")) {
        url = url.removeSuffix(".git")
    }

    // Do not try to remove the port if the URL uses the SSH protocol in the SCP syntax. For example
    // 'git@github.com:foo.git'. This syntax does not support port definitions. Attempting to remove the port
    // will result in an invalid URL when the repository name is made up of digits.
    // See https://github.com/ben-gibson/GitLink/issues/94
    if (!url.startsWith("git@")) {
        url = url.replace(":\\d{1,5}".toRegex(), ""); // remove the port
    }

    // Hack for azure
    if (url.startsWith("git@ssh.dev.azure.com")) {
        url = url.replace("^git@ssh.".toRegex(), "git@")
            .replace(":v\\d{1,5}".toRegex(), "") // remove v3 from git@ssh.dev.azure.com:v3/ben-gibson/test/test
    }

    if (!url.startsWith("http")) {
        url = url
            .replace("git@", "")
            .replace(Regex("^[^:]+://"), "")
            .replace(":", "/")

        url = "http://".plus(url)
    }

    return URL.fromString(url)
}

fun GitRemote.contains(repository: GitRepository, branch: GitLocalBranch): Boolean {
    val result = Git.getInstance().lsRemote(
        repository.project,
        repository.root,
        this,
        this.firstUrl,
        branch.fullName,
        "--heads"
    )

    if (result.success()) {
        return result.output.size == 1
    }

    return branch.findTrackedBranch(repository) != null
}

fun GitRemote.contains(repository: GitRepository, commit: Commit): Boolean {
    val command = GitLineHandler(repository.project, repository.root, GitCommand.BRANCH)

    command.addParameters("-r", "--contains", commit.toString())

    val result: GitCommandResult = Git.getInstance().runCommand(command)

    if (!result.success()) {
        return false
    }

    return result.output.find{ it.trim().startsWith(name) } != null
}
