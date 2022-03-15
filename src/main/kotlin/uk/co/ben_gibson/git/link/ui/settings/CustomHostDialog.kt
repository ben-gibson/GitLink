package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import uk.co.ben_gibson.git.link.Settings

class CustomHostDialog : DialogWrapper(true) {
    var name: String = ""
    var baseUrl: String = ""
    var fileAtBranchTemplate: String = ""
    var fileAtCommitTemplate: String = ""
    var commitTemplate: String = ""

    init {
        title = "Test DialogWrapper"
        setOKButtonText("Add")
        init()
    }

    override fun createCenterPanel() = panel {
        row("Name") {
            textField(::name).focused().withValidationOnInput(::validateName)
        }
        row("Base URL") {
            textField(::baseUrl).withValidationOnInput(::validateBaseUrl)
        }
        row("File at branch template") {
            textField(::fileAtBranchTemplate).withValidationOnInput(::validateTemplate)
        }
        row("File at commit template") {
            textField(::fileAtCommitTemplate).withValidationOnInput(::validateTemplate)
        }
        row("commit template") {
            textField(::commitTemplate).withValidationOnInput(::validateTemplate)
        }
    }

    fun getCustomHostSettings() = Settings.CustomHostSettings(
        name.lowercase().trim().replace("\\s", "-"),
        name.trim(),
        baseUrl,
        listOf()
    );

    private fun validateName(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error("Foo bar")
                else -> null
            }
        }
    }

    private fun validateBaseUrl(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error("Foo bar")
                else -> null
            }
        }
    }

    private fun validateTemplate(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error("Foo bar")
                else -> null
            }
        }
    }
}