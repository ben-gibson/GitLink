package uk.co.ben_gibson.git.link.git

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class CommitTest {

    @Test
    fun `should create commit with valid hash`() {
        // Given
        val hash = "b032a0707beac9a2f24b1b7d97ee4f7156de182c"

        // When
        val commit = Commit(hash)

        // Then
        assertThat(commit.toString()).isEqualTo(hash)
    }

    @Test
    fun `should generate short hash correctly`() {
        // Given
        val hash = "b032a0707beac9a2f24b1b7d97ee4f7156de182c"
        val commit = Commit(hash)

        // When
        val shortHash = commit.shortHash

        // Then
        assertThat(shortHash)
            .hasSize(6)
            .isEqualTo("b032a0")
            .isEqualTo(hash.substring(0, 6))
    }

    @Test
    fun `should handle short hash for minimum length`() {
        // Given
        val hash = "abcdef"
        val commit = Commit(hash)

        // When
        val shortHash = commit.shortHash

        // Then
        assertThat(shortHash).isEqualTo("abcdef")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "  ", "\t", "\n"])
    fun `should reject blank hash`(invalidHash: String) {
        assertThatThrownBy { Commit(invalidHash) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Commit hash cannot be blank")
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "ab", "abc", "abcd", "abcde"])
    fun `should reject short hash`(invalidHash: String) {
        assertThatThrownBy { Commit(invalidHash) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Commit hash must be at least 6 characters")
    }

    @Test
    fun `should support equality`() {
        // Given
        val hash = "b032a0707beac9a2f24b1b7d97ee4f7156de182c"
        val commit1 = Commit(hash)
        val commit2 = Commit(hash)
        val commit3 = Commit("different0707beac9a2f24b1b7d97ee4f7156de182c")

        // Then
        assertThat(commit1)
            .isEqualTo(commit2)
            .hasSameHashCodeAs(commit2)
            .isNotEqualTo(commit3)
    }

    @Test
    fun `should be usable in collections`() {
        // Given
        val commit1 = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        val commit2 = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")
        val commit3 = Commit("different0707beac9a2f24b1b7d97ee4f7156de182c")

        // When
        val set = setOf(commit1, commit2, commit3)

        // Then
        assertThat(set).hasSize(2)
        assertThat(set.contains(commit1)).isTrue()
        assertThat(set.contains(commit3)).isTrue()
    }
}

