package uk.co.ben_gibson.git.link.ui

import com.intellij.openapi.editor.Editor

val Editor.lineSelection: LineSelection
    get() {
        val caretStates = caretModel.caretsAndSelections

        if (caretStates.size < 1) {
            return LineSelection(caretModel.logicalPosition.line + 1)
        }

        val caretState = caretStates[0]

        val start = caretState.selectionStart
        val end = caretState.selectionEnd

        if (start == null || end == null) {
            return LineSelection(caretModel.logicalPosition.line + 1)
        }

        val startLine = start.line + 1
        var endLine = end.line + 1

        if (end.column == 0 && start != end) {
            endLine--
        }

        return LineSelection(startLine, endLine)
    }
