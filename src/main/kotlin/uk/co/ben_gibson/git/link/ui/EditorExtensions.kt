package uk.co.ben_gibson.git.link.ui

import com.intellij.openapi.editor.Editor

val Editor.lineSelection: LineSelection
    get() {
        val primaryCaret = caretModel.primaryCaret

        val start = primaryCaret.selectionStartPosition.line + 1
        val end = primaryCaret.selectionEndPosition.line + 1

        return LineSelection(start, end)
    }