package uk.co.ben_gibson.git.link.editor

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.SelectionModel
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import uk.co.ben_gibson.git.link.git.LineSelection

class EditorExtensionsTest {

    @ParameterizedTest(name = "{6}")
    @CsvSource(
        // startLine, startColumn, endLine, endColumn, expectedStart, expectedEnd, description
        "0, 0, 1, 0, 1, 1, a single whole line",
        "2, 4, 2, 9, 3, 3, part of a single line",
        "1, 0, 4, 0, 2, 4, several whole lines",
        "1, 0, 3, 5, 2, 4, several lines ending mid line",
        "1, 6, 2, 3, 2, 3, several lines starting mid line",
    )
    fun `should convert the selected logical positions to one based line numbers`(
        startLine: Int,
        startColumn: Int,
        endLine: Int,
        endColumn: Int,
        expectedStart: Int,
        expectedEnd: Int,
        description: String
    ) {
        // Given
        val editor = editorWithSelection(
            LogicalPosition(startLine, startColumn),
            LogicalPosition(endLine, endColumn)
        )

        // When
        val lineSelection = editor.lineSelection

        // Then
        assertThat(lineSelection).describedAs(description).isEqualTo(LineSelection(expectedStart, expectedEnd))
    }

    @Test
    fun `should not return a line selection when nothing is selected`() {
        // Given
        val editor: Editor = mockk()
        val selectionModel: SelectionModel = mockk()

        every { editor.selectionModel } returns selectionModel
        every { selectionModel.hasSelection() } returns false

        // When
        val lineSelection = editor.lineSelection

        // Then
        assertThat(lineSelection).isNull()
    }

    private fun editorWithSelection(start: LogicalPosition, end: LogicalPosition): Editor {
        val editor: Editor = mockk()
        val selectionModel: SelectionModel = mockk()

        // The offsets are arbitrary, they only exist to map back to the logical positions under test
        every { editor.selectionModel } returns selectionModel
        every { selectionModel.hasSelection() } returns true
        every { selectionModel.selectionStart } returns START_OFFSET
        every { selectionModel.selectionEnd } returns END_OFFSET
        every { editor.offsetToLogicalPosition(START_OFFSET) } returns start
        every { editor.offsetToLogicalPosition(END_OFFSET) } returns end

        return editor
    }

    companion object {
        private const val START_OFFSET = 0
        private const val END_OFFSET = 1
    }
}
