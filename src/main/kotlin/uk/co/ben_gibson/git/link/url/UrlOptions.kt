package uk.co.ben_gibson.git.link.url

import uk.co.ben_gibson.git.link.git.Commit as GitCommit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.git.LineSelection

sealed interface UrlOptions {
    class Commit(val commit: GitCommit, val currentBranch: String) : UrlOptions
    class FileAtCommit(val file: File, val currentBranch: String, val commit: GitCommit, val lineSelection: LineSelection? = null) : UrlOptions
    class FileAtBranch(val file: File, val branch: String, val lineSelection: LineSelection? = null) : UrlOptions
}
