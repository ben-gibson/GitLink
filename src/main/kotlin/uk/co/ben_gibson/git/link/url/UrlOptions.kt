package uk.co.ben_gibson.git.link.url

import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import java.net.URL

interface LineSelectionAware {
    val lineSelection: LineSelection?
}

sealed class UrlOptions(val baseUrl: URL)

class UrlOptionsCommit(baseUrl: URL, val commit: Commit) : UrlOptions(baseUrl)

class UrlOptionsFileAtCommit(
    baseUrl: URL,
    val file: File,
    val commit: Commit,
    override val lineSelection: LineSelection? = null
) : UrlOptions(baseUrl), LineSelectionAware

class UrlOptionsFileAtBranch(
    baseUrl: URL,
    val file: File,
    val branch: String,
    override val lineSelection: LineSelection? = null
) : UrlOptions(baseUrl), LineSelectionAware