package uk.co.ben_gibson.git.link

import com.intellij.openapi.vfs.VirtualFile
import uk.co.ben_gibson.git.link.git.Commit as GitCommit
import uk.co.ben_gibson.git.link.git.LineSelection

// [repositoryFile] is used only to resolve which Git repository the action was triggered from -
// it isn't necessarily the file being linked to (see Commit.root).
sealed class Context(val repositoryFile: VirtualFile) {
    data class Commit(
        val root: VirtualFile,
        val commit: GitCommit
    ) : Context(root)

    data class FileAtCommit(
        val file: VirtualFile,
        val commit: GitCommit,
        val lineSelection: LineSelection? = null
    ) : Context(file)

    data class File(
        val file: VirtualFile,
        val lineSelection: LineSelection? = null
    ) : Context(file)
}
