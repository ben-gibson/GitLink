package uk.co.ben_gibson.git.link.ui

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class LineSelectionTest {

    @Test
    fun `should create line selection with start and end`() {
        // Given & When
        val selection = LineSelection(10, 20)

        // Then
        assertThat(selection.start).isEqualTo(10)
        assertThat(selection.end).isEqualTo(20)
        assertThat(selection.lineCount).isEqualTo(11) // 10 to 20 inclusive
    }

    @Test
    fun `should create single line selection`() {
        // Given & When
        val selection = LineSelection(42)

        // Then
        assertThat(selection.start).isEqualTo(42)
        assertThat(selection.end).isEqualTo(42)
        assertThat(selection.lineCount).isEqualTo(1)
    }

    @ParameterizedTest
    @CsvSource(
        "1, 1, 1",
        "1, 5, 5",
        "10, 20, 11",
        "100, 200, 101"
    )
    fun `should calculate line count correctly`(start: Int, end: Int, expectedCount: Int) {
        // Given
        val selection = LineSelection(start, end)

        // When
        val count = selection.lineCount

        // Then
        assertThat(count).isEqualTo(expectedCount)
    }

    @ParameterizedTest
    @CsvSource(
        "0, 10",
        "-1, 10",
        "-5, -1"
    )
    fun `should reject non-positive start line`(start: Int, end: Int) {
        assertThatThrownBy { LineSelection(start, end) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Start line must be positive")
    }

    @ParameterizedTest
    @CsvSource(
        "10, 0",
        "10, -1",
        "5, -5"
    )
    fun `should reject non-positive end line`(start: Int, end: Int) {
        assertThatThrownBy { LineSelection(start, end) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("End line must be positive")
    }

    @Test
    fun `should reject start line greater than end line`() {
        assertThatThrownBy { LineSelection(20, 10) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Start line (20) must be <= end line (10)")
    }

    @Test
    fun `should support equality comparison`() {
        // Given
        val selection1 = LineSelection(10, 20)
        val selection2 = LineSelection(10, 20)
        val selection3 = LineSelection(10, 21)

        // Then
        assertThat(selection1)
            .isEqualTo(selection2)
            .hasSameHashCodeAs(selection2)
            .isNotEqualTo(selection3)
    }

    @Test
    fun `should be usable in collections`() {
        // Given
        val selection1 = LineSelection(10, 20)
        val selection2 = LineSelection(10, 20)
        val selection3 = LineSelection(30, 40)

        // When
        val set = setOf(selection1, selection2, selection3)

        // Then
        assertThat(set).hasSize(2)
        assertThat(set.contains(selection1)).isTrue()
        assertThat(set.contains(selection3)).isTrue()
    }
}

