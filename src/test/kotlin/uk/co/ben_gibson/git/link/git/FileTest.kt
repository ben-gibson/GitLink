package uk.co.ben_gibson.git.link.git

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FileTest {

    @Test
    fun `should create file with all properties`() {
        // Given & When
        val file = File(
            name = "MyClass.kt",
            isDirectory = false,
            path = "src/main/kotlin/com/example",
            isRoot = false
        )

        // Then
        assertThat(file.name).isEqualTo("MyClass.kt")
        assertThat(file.isDirectory).isFalse()
        assertThat(file.path).isEqualTo("src/main/kotlin/com/example")
        assertThat(file.isRoot).isFalse()
    }

    @Test
    fun `should create directory`() {
        // Given & When
        val directory = File(
            name = "resources",
            isDirectory = true,
            path = "src/main",
            isRoot = false
        )

        // Then
        assertThat(directory.isDirectory).isTrue()
    }

    @Test
    fun `should create root directory`() {
        // Given & When
        val root = File(
            name = "project",
            isDirectory = true,
            path = "",
            isRoot = true
        )

        // Then
        assertThat(root.isRoot).isTrue()
        assertThat(root.path).isEmpty()
    }

    @Test
    fun `should support equality comparison`() {
        // Given
        val file1 = File("Test.kt", false, "src", false)
        val file2 = File("Test.kt", false, "src", false)
        val file3 = File("Other.kt", false, "src", false)

        // Then
        assertThat(file1)
            .isEqualTo(file2)
            .hasSameHashCodeAs(file2)
            .isNotEqualTo(file3)
    }

    @Test
    fun `should be usable in collections`() {
        // Given
        val file1 = File("Test.kt", false, "src", false)
        val file2 = File("Test.kt", false, "src", false)
        val file3 = File("Other.kt", false, "src", false)

        // When
        val set = setOf(file1, file2, file3)

        // Then
        assertThat(set).hasSize(2)
        assertThat(set.contains(file1)).isTrue()
        assertThat(set.contains(file3)).isTrue()
    }
}

