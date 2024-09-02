package uk.co.ben_gibson.git.link.url

import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection

sealed interface UrlOptions {
    class UrlOptionsCommit(val commit: Commit, val currentBranch: String,) : UrlOptions
    class UrlOptionsFileAtCommit(val file: File, val currentBranch: String, val commit: Commit, val lineSelection: LineSelection? = null) : UrlOptions
    class UrlOptionsFileAtBranch(val file: File, val branch: String, val lineSelection: LineSelection? = null) : UrlOptions
}