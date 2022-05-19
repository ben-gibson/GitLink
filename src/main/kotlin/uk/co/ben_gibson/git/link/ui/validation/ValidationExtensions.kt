package uk.co.ben_gibson.git.link.ui.validation

import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.layout.ValidationInfoBuilder
import uk.co.ben_gibson.git.link.GitLinkBundle.message
import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.*
import uk.co.ben_gibson.git.link.url.factory.TemplatedUrlFactory
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException

fun ValidationInfoBuilder.notBlank(value: String): ValidationInfo? = if (value.isEmpty()) error(message("validation.required")) else null

fun ValidationInfoBuilder.domain(value: String): ValidationInfo? {
    if (value.isEmpty()) {
        return null
    }

    val normalised = value.takeIf { it.startsWith("http") } ?: "https://".plus(value)

    return try {
        val domain = URI(normalised)
        if (domain.host != value) error(message("validation.invalid-domain")) else null
    } catch (e: URISyntaxException) {
        error(message("validation.invalid-domain"))
    }
}

fun ValidationInfoBuilder.alphaNumeric(value: String): ValidationInfo? {
    if (value.isEmpty()) {
        return null
    }

    return if (!value.matches("[\\w\\s]+".toRegex())) error(message("validation.alpha-numeric")) else null
}

fun ValidationInfoBuilder.exists(value: String, existing: Collection<String>): ValidationInfo? {
    if (value.isEmpty()) {
        return null
    }

    return if (existing.contains(value)) return error(message("validation.exists")) else null
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
        URI("https://example.com"),
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
        URI("https://example.com"),
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
        URI("https://example.com"),
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