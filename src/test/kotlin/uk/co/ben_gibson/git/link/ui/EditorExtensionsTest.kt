package uk.co.ben_gibson.git.link.ui

import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.CaretState
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditorExtensionsTest {

    @Test
    fun `test line selection with trailing newline`() {
        // Arrange
        val editor: Editor = mockk()
        val caretModel: CaretModel = mockk()
        val caretState: CaretState = mockk()

        val startPosition = LogicalPosition(1, 0) // Line 2, column 0
        val endPosition = LogicalPosition(4, 0)   // Line 5, column 0

        every { editor.caretModel } returns caretModel
        every { caretModel.caretsAndSelections } returns listOf(caretState)
        every { caretState.selectionStart } returns startPosition
        every { caretState.selectionEnd } returns endPosition

        // Act
        val lineSelection = editor.lineSelection

        // Assert
        assertEquals(2, lineSelection.start)
        assertEquals(4, lineSelection.end)
    }

    @Test
    fun `test line selection without trailing newline`() {
        // Arrange
        val editor: Editor = mockk()
        val caretModel: CaretModel = mockk()
        val caretState: CaretState = mockk()

        val startPosition = LogicalPosition(1, 0) // Line 2, column 0
        val endPosition = LogicalPosition(3, 5)   // Line 4, column 5

        every { editor.caretModel } returns caretModel
        every { caretModel.caretsAndSelections } returns listOf(caretState)
        every { caretState.selectionStart } returns startPosition
        every { caretState.selectionEnd } returns endPosition

        // Act
        val lineSelection = editor.lineSelection

        // Assert
        assertEquals(2, lineSelection.start)
        assertEquals(4, lineSelection.end)
    }
}
