package uk.co.ben_gibson.git.link.ui.validation

import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.layout.ValidationInfoBuilder
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.UrlOptions
import uk.co.ben_gibson.git.link.url.UrlOptionsCommit
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtBranch
import uk.co.ben_gibson.git.link.url.UrlOptionsFileAtCommit
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import java.net.MalformedURLException
import java.net.URL

fun ValidationInfoBuilder.notBlank(value: String): ValidationInfo? = if (value.isEmpty()) error(message("validation.required")) else null

fun ValidationInfoBuilder.url(value: String): ValidationInfo? {
    if (value.isEmpty()) {
        return null
    }

    try {
        URL(value)
    } catch (e: MalformedURLException) {
        return error(message("validation.invalid-url"))
    }

    return null
}

fun ValidationInfoBuilder.alphaNumeric(value: String): ValidationInfo? {
    if (value.isEmpty()) {
        return null
    }

    return if (!value.matches("[\\w\\s]+".toRegex())) error(message("validation.alpha-numeric")) else null
}

fun ValidationInfoBuilder.length(value: String, min: Int, max: Int): ValidationInfo? {
    if (value.isEmpty()) {
        return null;
    }

    return when {
        value.length < min -> error(message("validation.min-length", min))
        value.length > max -> error(message("validation.max-length", max))
        else -> null
    }
}

fun ValidationInfoBuilder.fileAtCommitTemplate(value: String): ValidationInfo? {
    if (value.isEmpty()) {
        return null;
    }

    val options = UrlOptionsFileAtCommit(
        URL("https://example.com"),
        File("foo.kt", false, "src/main", false),
        Commit("734232a3c18f0625843bd161c3f5da272b9d53c1"),
        LineSelection(10, 20)
    )

    return urlTemplate(options, fileAtCommit = value)
}

fun ValidationInfoBuilder.fileAtBranchTemplate(value: String): ValidationInfo? {
    if (value.isEmpty()) {
        return null;
    }

    val options = UrlOptionsFileAtBranch(
        URL("https://example.com"),
        File("foo.kt", false, "src/main", false),
        "master",
        LineSelection(10, 20)
    )

    return urlTemplate(options, fileAtBranch = value)
}

fun ValidationInfoBuilder.commitTemplate(value: String): ValidationInfo? {
    if (value.isEmpty()) {
        return null;
    }

    val options = UrlOptionsCommit(
        URL("https://example.com"),
        Commit("734232a3c18f0625843bd161c3f5da272b9d53c1")
    )

    return urlTemplate(options, commit = value);
}

private fun ValidationInfoBuilder.urlTemplate(
    options: UrlOptions,
    fileAtBranch: String = "",
    fileAtCommit: String = "",
    commit: String = ""
) : ValidationInfo? {
    val factory = TemplatedUrlFactory(UrlTemplates(fileAtBranch, fileAtCommit, commit));

    return try {
        factory.createUrl(options)
        null
    } catch (e: MalformedURLException) {
        error(message("validation.invalid-url-template"))
    }
}