package uk.co.ben_gibson.git.link.url

import com.intellij.openapi.vfs.VirtualFile
import uk.co.ben_gibson.git.link.git.Commit

enum class Type {
    COMMIT,
    FILE
}

data class Context(
    val type: Type,
    val file: VirtualFile,
    val commit: Commit? = null,
    val lineSelection: LineSelection? = null
)

data class LineSelection(
    val start: Int,
    val end: Int
)
