package uk.co.ben_gibson.git.link

import com.intellij.openapi.vfs.VirtualFile
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.ui.LineSelection

sealed class Context(open val file: VirtualFile)

data class ContextCommit(
    override val file: VirtualFile,
    val commit: Commit
) : Context(file)

data class ContextFileAtCommit(
    override val file: VirtualFile,
    val commit: Commit,
    val lineSelection: LineSelection? = null
) : Context(file)

data class ContextCurrentFile(
    override val file: VirtualFile,
    val lineSelection: LineSelection? = null
) : Context(file)
