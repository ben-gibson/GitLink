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

        return LineSelection(start.line + 1, end.line + 1)
    }