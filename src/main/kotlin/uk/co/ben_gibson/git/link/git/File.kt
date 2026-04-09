package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRepository

data class File(
    val name: String,
    val isDirectory: Boolean,
    val path: String,
    val isRoot: Boolean
) {
    companion object {
        fun forRepository(file: VirtualFile, repository: GitRepository): File {
            val isRoot = file.path == repository.root.path
            val relativePath = if (isRoot) {
                ""
            } else {
                file.path.substring(repository.root.path.length)
                    .removeSuffix("/${file.name}")
                    .trim('/')
            }

            return File(
                name = file.name,
                isDirectory = file.isDirectory,
                path = relativePath,
                isRoot = isRoot
            )
        }
    }
}
