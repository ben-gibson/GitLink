package uk.co.ben_gibson.git.link.editor

import com.intellij.openapi.editor.Editor
import uk.co.ben_gibson.git.link.git.LineSelection

val Editor.lineSelection: LineSelection?
    get() {
        if (!selectionModel.hasSelection()) {
            return null
        }

        val start = offsetToLogicalPosition(selectionModel.selectionStart)
        val end = offsetToLogicalPosition(selectionModel.selectionEnd)

        // Logical positions are zero based, whereas line numbers in a URL are one based
        val startLine = start.line + 1
        var endLine = end.line + 1

        // A whole line selection ends at column zero of the following line, which isn't itself selected
        if (end.column == 0) {
            endLine--
        }

        return LineSelection(startLine, endLine)
    }
