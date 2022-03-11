package uk.co.ben_gibson.git.link.url

import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import java.net.URL

sealed class UrlOptions(val baseUrl: URL)

class UrlOptionsCommit(
    baseUrl: URL,
    val commit: Commit,
    val lineSelection: LineSelection? = null
) : UrlOptions(baseUrl)

class UrlOptionsFileAtCommit(
    baseUrl: URL,
    val file: File,
    val commit: Commit,
    val lineSelection: LineSelection? = null
) : UrlOptions(baseUrl)

class UrlOptionsFileAtBranch(
    baseUrl: URL,
    val file: File,
    val branch: String,
    val lineSelection: LineSelection? = null
) : UrlOptions(baseUrl)