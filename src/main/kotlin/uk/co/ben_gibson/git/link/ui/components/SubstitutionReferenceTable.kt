package uk.co.ben_gibson.git.link.ui.components

import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel

private val references = listOf(
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
        "example.com"
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

class SubstitutionReferenceTable: TableView<SubstitutionReference>(
    ListTableModel(
        arrayOf(
            SubstitutionColumnInfo("Substitution") { it.substitution },
            SubstitutionColumnInfo("Description") { it.description },
            SubstitutionColumnInfo("Example") { it.example }
        ),
        references
    )
)

private class SubstitutionColumnInfo(name: String, val formatter: (SubstitutionReference) -> String) :
    ColumnInfo<SubstitutionReference, String>(name) {
    override fun valueOf(item: SubstitutionReference): String {
        return formatter(item)
    }
}

data class SubstitutionReference(val substitution: String, val description: String, val example: String)