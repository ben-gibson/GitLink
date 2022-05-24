package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRepository

/**
 * Represents a repository file, where the path is relative to the repository it lives in.
 */
data class File(val name: String, val isDirectory: Boolean, val path: String, val isRoot: Boolean) {
    companion object {
        fun forRepository(file: VirtualFile, repository: GitRepository): File {
            return File(
                file.name,
                file.isDirectory,
                file.path.substring(repository.root.path.length).replace(file.name, "").trim('/'),
                file.path == repository.root.path
            )
        }
    }
}
