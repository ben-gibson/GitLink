package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel

class CustomHostDialog : DialogWrapper(true) {
    var foo: String = "foo"

    init {
        title = "Test DialogWrapper"
        init()
    }

    override fun createCenterPanel() = panel {
        row("Name") {
            textField(::foo)
        }
    }
}