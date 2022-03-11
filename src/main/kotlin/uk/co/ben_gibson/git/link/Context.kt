package uk.co.ben_gibson.git.link

import com.intellij.openapi.vfs.VirtualFile
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.ui.LineSelection

sealed class Context(val file: VirtualFile)

class ContextCommit(
    file: VirtualFile,
    val commit: Commit,
    val lineSelection: LineSelection? = null
) : Context(file)

class ContextFileAtCommit(
    file: VirtualFile,
    val commit: Commit,
    val lineSelection: LineSelection? = null
) : Context(file)

class ContextFileAtBranch(
    file: VirtualFile,
    val branch: String,
    val lineSelection: LineSelection? = null
) : Context(file)

class ContextCurrentFile(file: VirtualFile, val lineSelection: LineSelection? = null) : Context(file)
