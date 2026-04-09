package uk.co.ben_gibson.git.link.git

import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class FileTest {

    @Nested
    inner class ForRepository {

        @Test
        fun `should calculate relative path for nested file`() {
            // Given
            val file = virtualFile("/project/src/main/MyClass.kt", "MyClass.kt")
            val repository = repository("/project")

            // When
            val result = File.forRepository(file, repository)

            // Then
            assertThat(result.name).isEqualTo("MyClass.kt")
            assertThat(result.path).isEqualTo("src/main")
            assertThat(result.isDirectory).isFalse()
            assertThat(result.isRoot).isFalse()
        }

        @Test
        fun `should return empty path for root directory`() {
            // Given
            val file = virtualFile("/project", "project", isDirectory = true)
            val repository = repository("/project")

            // When
            val result = File.forRepository(file, repository)

            // Then
            assertThat(result.path).isEmpty()
            assertThat(result.isRoot).isTrue()
        }

        @Test
        fun `should return empty path for file directly in repo root`() {
            // Given
            val file = virtualFile("/project/README.md", "README.md")
            val repository = repository("/project")

            // When
            val result = File.forRepository(file, repository)

            // Then
            assertThat(result.path).isEmpty()
            assertThat(result.isRoot).isFalse()
        }

        @Test
        fun `should not replace file name when it appears in directory path`() {
            // Given — file name "src" also appears as a directory component
            val file = virtualFile("/project/src/main/src", "src", isDirectory = true)
            val repository = repository("/project")

            // When
            val result = File.forRepository(file, repository)

            // Then
            assertThat(result.path).isEqualTo("src/main")
        }

        @Test
        fun `should handle deeply nested files`() {
            // Given
            val file = virtualFile("/project/a/b/c/d/File.kt", "File.kt")
            val repository = repository("/project")

            // When
            val result = File.forRepository(file, repository)

            // Then
            assertThat(result.path).isEqualTo("a/b/c/d")
        }

        @Test
        fun `should handle directory in subdirectory`() {
            // Given
            val file = virtualFile("/project/src/resources", "resources", isDirectory = true)
            val repository = repository("/project")

            // When
            val result = File.forRepository(file, repository)

            // Then
            assertThat(result.path).isEqualTo("src")
            assertThat(result.isDirectory).isTrue()
        }

        private fun virtualFile(path: String, name: String, isDirectory: Boolean = false): VirtualFile {
            return mockk {
                every { this@mockk.path } returns path
                every { this@mockk.name } returns name
                every { this@mockk.isDirectory } returns isDirectory
            }
        }

        private fun repository(rootPath: String): GitRepository {
            val root = mockk<VirtualFile> {
                every { path } returns rootPath
            }
            return mockk {
                every { this@mockk.root } returns root
            }
        }
    }
}
