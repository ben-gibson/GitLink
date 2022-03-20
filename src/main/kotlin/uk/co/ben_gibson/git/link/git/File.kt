package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRepository

/**
 * Represents a repository file, where the path is relative to the repository it lives in.
 */
data class File constructor(private val file: VirtualFile, private val root: VirtualFile) {
    val isDirectory = file.isDirectory
    val name = file.name
    val path = file.path.substring(root.path.length).replace(name, "").trimEnd('/')
    val isRoot = file.path == root.path

    companion object {
        fun create(file: VirtualFile, repository: GitRepository): File {
            return File(file, repository.root)
        }
    }
}
