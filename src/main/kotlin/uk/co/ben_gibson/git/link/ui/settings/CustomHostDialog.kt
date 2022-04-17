package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.settings.ApplicationSettings.CustomHostSettings
import uk.co.ben_gibson.git.link.ui.validation.isFileAtBranchTemplateValid
import uk.co.ben_gibson.git.link.ui.validation.isFileAtCommitTemplateValid
import java.net.MalformedURLException
import java.net.URL

class CustomHostDialog(val customHost: CustomHostSettings) : DialogWrapper(false) {

    init {
        title = message("settings.custom-host.add-dialog.title")
        setOKButtonText(message("actions.add"))
        setSize(700, 700)
        init()
    }

    override fun createCenterPanel() = panel {
        row(message("settings.custom-host.add-dialog.field.name.label")) {
            textField(customHost::displayName)
                .focused()
                .withValidationOnApply(::validateName)
                .comment(message("settings.custom-host.add-dialog.field.name.comment"))
        }
        row(message("settings.custom-host.add-dialog.field.base-url.label")) {
            textField(customHost::baseUrl)
                .withValidationOnApply(::validateBaseUrl)
                .comment(message("settings.custom-host.add-dialog.field.base-url.comment"))
        }
        row(message("settings.custom-host.add-dialog.field.file-at-branch-template.label")) {
            textField(customHost::fileAtBranchTemplate)
                .withValidationOnApply(::validateCommitTemplate)
                .comment(message("settings.custom-host.add-dialog.field.file-at-branch-template.comment"))
        }
        row(message("settings.custom-host.add-dialog.field.file-at-commit-template.label")) {
            textField(customHost::fileAtCommitTemplate)
                .withValidationOnApply(::validateCommitTemplate)
                .comment(message("settings.custom-host.add-dialog.field.file-at-commit-template.comment"))
        }
        row(message("settings.custom-host.add-dialog.field.commit-template.label")) {
            textField(customHost::commitTemplate)
                .withValidationOnApply(::validateCommitTemplate)
                .comment(message("settings.custom-host.add-dialog.field.commit-template.comment"))
        }
        row() {
            scrollPane(createSubstitutionReferenceTable())
        }
    }

    private fun validateName(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text.trim()
            when {
                fieldText.isEmpty() -> error(message("validation.required"))
                !fieldText.matches("[\\w\\s]+".toRegex()) -> error(message("validation.alpha-numeric"))
                fieldText.length < 3 -> error(message("validation.min-length", 3))
                fieldText.length > 15 -> error(message("validation.max-length", 15))
                else -> null
            }
        }
    }

    private fun validateBaseUrl(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error(message("validation.required"))
                else -> {
                    try {
                        URL(fieldText)
                    } catch (e: MalformedURLException) {
                        return error(message("validation.invalid-url"))
                    }

                    return null
                }
            }
        }
    }

    private fun validateFileAtCommitTemplate(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error(message("validation.required"))
                !isFileAtCommitTemplateValid(fieldText) -> error(message("validation.invalid-url-template"))
                else -> null
            }
        }
    }

    private fun validateFileAtBranchTemplate(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error(message("validation.required"))
                !isFileAtBranchTemplateValid(fieldText) -> error(message("validation.invalid-url-template"))
                else -> null
            }
        }
    }

    private fun validateCommitTemplate(builder: ValidationInfoBuilder, textField: JBTextField): ValidationInfo? {
        return builder.run {
            val fieldText = textField.text
            when {
                fieldText.isEmpty() -> error(message("validation.required"))
                !isFileAtBranchTemplateValid(fieldText) -> error(message("validation.invalid-url-template"))
                else -> null
            }
        }
    }

    private fun createSubstitutionReferenceTable() = TableView<SubstitutionReference>(ListTableModel(
        arrayOf(
            SubstitutionColumnInfo("Substitution") { it.substitution },
            SubstitutionColumnInfo("Description") { it.description },
            SubstitutionColumnInfo("Example") { it.example }
        ),
        listOf(
            SubstitutionReference(
                "{commit}",
                "The complete hash of the commit.",
                "05fc48765f69d52aa229fc5edc3842ab3d9ff517"
            ),
            SubstitutionReference(
                "{commit:short}",
                "The first 6 characters of the commit hash.",
                "05fc48"
            ),
            SubstitutionReference(
                "{branch}",
                "The branch.",
                "master"
            ),
            SubstitutionReference(
                "{file:name}",
                "The selected file name.",
                "NotificationDispatcher.java"
            ),
            SubstitutionReference(
                "{file:path}",
                "The selected file path.",
                "src/uk/co/ben_gibson/git/link"
            ),
            SubstitutionReference(
                "{line:start}",
                "The line selection start.",
                "10"
            ),
            SubstitutionReference(
                "{line:end}",
                "The line selection end.",
                "30"
            ),
            SubstitutionReference(
                "{remote:url}",
                "The full remote url.",
                "https://example.com/ben-gibson/super-project"
            ),
            SubstitutionReference(
                "{remote:url:host}",
                "The remote url host.",
                "https://example.com"
            ),
            SubstitutionReference(
                "{remote:url:path}",
                "The remote url path.",
                "ben-gibson/super-project"
            ),
            SubstitutionReference(
                "{remote:url:path:n}",
                "A specific part of the remote url path starting at 0.",
                "super-project"
            ),
        )
    )).apply {
        setShowColumns(true)
    }

    private class SubstitutionColumnInfo (name: String, val formatter: (SubstitutionReference) -> String) :
        ColumnInfo<SubstitutionReference, String>(name)
    {
        override fun valueOf(item: SubstitutionReference): String {
            return formatter(item)
        }
    }

    private data class SubstitutionReference(val substitution: String, val description: String, val example: String)
}