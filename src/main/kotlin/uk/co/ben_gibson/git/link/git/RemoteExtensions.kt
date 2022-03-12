package uk.co.ben_gibson.git.link.git

import git4idea.repo.GitRemote
import java.net.URL

fun GitRemote.httpUrl() : URL? {
    var url = firstUrl ?: return null

    url = url
        .trim()
        .removeSuffix(".git")

    // Do not try to remove the port if the URL uses the SSH protocol in the SCP syntax. For example
    // 'git@github.com:foo.git'. This syntax does not support port definitions. Attempting to remove the port
    // will result in an invalid URL when the repository name is made up of digits.
    // See https://github.com/ben-gibson/GitLink/issues/94
    if (!url.startsWith("git@")) {
        url = url.replace(":\\d{1,5}", ""); // remove the port
    }

    if (!url.startsWith("http")) {
        url = url
            .replace("git@", "")
            .replace("ssh://", "")
            .replace("git://", "")
            .replace(":", "/")

        url = "http://".plus(url)
    }

    return URL(url)
}

fun GitRemote.guessHost() = Host.forRemote(this)