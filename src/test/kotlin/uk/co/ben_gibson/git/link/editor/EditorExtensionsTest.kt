package uk.co.ben_gibson.git.link.editor

import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.testFramework.EditorTestUtil
import com.intellij.testFramework.junit5.RunInEdt
import com.intellij.testFramework.junit5.TestApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Named
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import uk.co.ben_gibson.git.link.git.LineSelection

@TestApplication
@RunInEdt(writeIntent = true)
class EditorExtensionsTest {

    private val editors = mutableListOf<Editor>()

    @AfterEach
    fun releaseEditors() {
        editors.forEach { EditorFactory.getInstance().releaseEditor(it) }
        editors.clear()
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("selections")
    fun `should convert the selection to one based line numbers`(text: String, expected: LineSelection) {
        // Given
        val editor = editor(text)

        // When
        val lineSelection = editor.lineSelection

        // Then
        assertThat(lineSelection).isEqualTo(expected)
    }

    @Test
    fun `should anchor to the caret line when nothing is selected`() {
        // Given
        val editor = editor(
            """
                line one
                line <caret>two
                line three
            """.trimIndent()
        )

        // When
        val lineSelection = editor.lineSelection

        // Then
        assertThat(lineSelection).isEqualTo(LineSelection(2, 2))
    }

    // Opens a real editor over the given text, with the <selection> and <caret> markers stripped out and applied
    private fun editor(text: String): Editor {
        val factory = EditorFactory.getInstance()
        val document = factory.createDocument(text)
        val markers = runWriteAction { EditorTestUtil.extractCaretAndSelectionMarkers(document) }
        val editor = factory.createEditor(document)

        editors.add(editor)
        EditorTestUtil.setCaretsAndSelection(editor, markers)

        return editor
    }

    companion object {
        @JvmStatic
        fun selections() = listOf(
            Arguments.of(
                Named.of(
                    "a single whole line",
                    """
                        <selection>line one
                        </selection>line two
                        line three
                        line four
                        line five
                    """.trimIndent()
                ),
                LineSelection(1, 1)
            ),
            Arguments.of(
                Named.of(
                    "part of a single line",
                    """
                        line one
                        line two
                        line<selection> thre</selection>e
                        line four
                        line five
                    """.trimIndent()
                ),
                LineSelection(3, 3)
            ),
            Arguments.of(
                Named.of(
                    "several whole lines",
                    """
                        line one
                        <selection>line two
                        line three
                        line four
                        </selection>line five
                    """.trimIndent()
                ),
                LineSelection(2, 4)
            ),
            Arguments.of(
                Named.of(
                    "several lines ending mid line",
                    """
                        line one
                        <selection>line two
                        line three
                        line </selection>four
                        line five
                    """.trimIndent()
                ),
                LineSelection(2, 4)
            ),
            Arguments.of(
                Named.of(
                    "several lines starting mid line",
                    """
                        line one
                        line t<selection>wo
                        lin</selection>e three
                        line four
                        line five
                    """.trimIndent()
                ),
                LineSelection(2, 3)
            )
        )
    }
}
