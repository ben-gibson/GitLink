package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import uk.co.ben_gibson.git.link.Settings
import java.net.MalformedURLException
import java.net.URL

class CustomHostDialog(val customHost: Settings.CustomHostSettings) : DialogWrapper(false) {
    init {
        title = "Test DialogWrapper"
        setOKButtonText("Add")
        setSize(600, 300)
        init()
    }

    override fun createCenterPanel() = panel {
        row("Name") {
            textField(customHost::displayName).focused().withValidationOnApply(::validateName)
        }
        row("Base URL") {
            textField(customHost::baseUrl).withValidationOnApply(::validateBaseUrl)
        }
        row("File at branch template") {
            textField(customHost::fileAtBranchTemplate).withValidationOnApply(::validateTemplate)
        }
        row("File at commit template") {
            textField(customHost::fileAtCommitTemplate).withValidationOnApply(::validateTemplate)
        }
        row("commit template") {
            textField(customHost::commitTemplate).withValidationOnApply(::validateTemplate)
        }
    }

    private fun validateName(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error("Name required")
                !fieldText.matches("\\w+".toRegex()) -> error("Name can only contain alphanumeric characters")
                fieldText.length < 3 -> error("Name must be at least 3 characters")
                fieldText.length > 15 -> error("Name cannot be more than 15 characters")
                else -> null
            }
        }
    }

    private fun validateBaseUrl(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error("Base URL Required")
                else -> {
                    try {
                        URL(fieldText)
                    } catch (e: MalformedURLException) {
                        return error("Invalid URL")
                    }

                    return null
                }
            }
        }
    }

    private fun validateTemplate(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error("Template required")
                else -> null
            }
        }
    }
}