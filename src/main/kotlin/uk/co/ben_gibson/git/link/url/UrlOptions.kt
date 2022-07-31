package uk.co.ben_gibson.git.link.url

import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.url.URL

sealed class UrlOptions(val baseUrl: URL)

class UrlOptionsCommit(baseUrl: URL, val commit: Commit) : UrlOptions(baseUrl)

interface UrlOptionsFileAware {
    val baseUrl: URL
    val file: File
    val ref: String
    val lineSelection: LineSelection?
}

class UrlOptionsFileAtCommit(
    baseUrl: URL,
    override val file: File,
    val commit: Commit,
    override val lineSelection: LineSelection? = null
) : UrlOptions(baseUrl), UrlOptionsFileAware {
    override val ref: String get() = commit.toString()
}

class UrlOptionsFileAtBranch(
    baseUrl: URL,
    override val file: File,
    val branch: String,
    override val lineSelection: LineSelection? = null
) : UrlOptions(baseUrl), UrlOptionsFileAware {
    override val ref: String get() = branch
}