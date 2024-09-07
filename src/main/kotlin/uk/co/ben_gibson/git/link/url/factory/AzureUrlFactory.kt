package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.*
import uk.co.ben_gibson.url.*

@Service
class AzureUrlFactory: UrlFactory {
    override fun createUrl(baseUrl: URL, options: UrlOptions): URL {
        val normalisedBaseUrl = normaliseBaseUrl(baseUrl)

        return when (options) {
            is UrlOptions.UrlOptionsFileAtBranch -> createUrlToFileAtBranch(normalisedBaseUrl, options)
            is UrlOptions.UrlOptionsFileAtCommit -> createUrlToFileAtCommit(normalisedBaseUrl, options)
            is UrlOptions.UrlOptionsCommit -> createUrlToCommit(normalisedBaseUrl, options)
        }
    }

    private fun createUrlToFileAtCommit(baseUrl: URL, options: UrlOptions.UrlOptionsFileAtCommit) : URL {
        var queryString = QueryString.fromMap(
            mapOf(
                "version" to listOf("GC".plus(options.commit)),
                "path" to listOf(createFileParameter(options.file))
            )
        )

        if (options.lineSelection != null) {
            queryString = addLineSelectionParameters(queryString, options.lineSelection)
        }

        return baseUrl.withQueryString(queryString)
    }

    private fun createUrlToFileAtBranch(baseUrl: URL, options: UrlOptions.UrlOptionsFileAtBranch) : URL {
        var queryString = QueryString.fromMap(
            mapOf(
                "version" to listOf("GB".plus(options.branch)),
                "path" to listOf(createFileParameter(options.file))
            )
        )

        if (options.lineSelection != null) {
            queryString = addLineSelectionParameters(queryString, options.lineSelection)
        }

        return baseUrl.withQueryString(queryString)
    }

    private fun createFileParameter(file: File) : String {
        val fileName = if (file.isRoot) "" else file.name

        return file.path.plus("/").plus(fileName)
    }

    private fun createUrlToCommit(baseUrl: URL, options: UrlOptions.UrlOptionsCommit): URL {
        val path = requireNotNull(baseUrl.path) { "Unexpected error: repository path must be present in remote URL" }
        return baseUrl.withPath(path.with(Path.fromSegments(listOf("commit", options.commit.toString()))))
    }

    private fun addLineSelectionParameters(queryString: QueryString, lineSelection: LineSelection) : QueryString {
        return queryString
            .withParameter("line", lineSelection.start.toString())
            .withParameter("lineEnd", (lineSelection.end + 1).toString())

            // TODO: Pass column selection in.
            .withParameter("lineStartColumn", "1")
            .withParameter("lineEndColumn", "1")
    }

    private fun normaliseBaseUrl(baseUrl: URL): URL {
        // Convert ssh.dev.azure.com:v3/ben-gibson/test/test to dev.azure.com:ben-gibson/test/_git/test.git
        val basePathParts = baseUrl.path
            .toString()
            .removePrefix("v3/")
            .split("/")
            .toMutableList()

        // Azure expects this to be in the path between the project and repo name. It's already included when cloning the project using HTTPS, but not when cloning the project using SSH.
        if (!basePathParts.contains("_git")) {
            // urls might have an option company component, if that's the case we need to insert _git in between company/project and repository parts
            val indexToAddGit = if (basePathParts.size >= 3) 2 else 1
            basePathParts.add(indexToAddGit, "_git")
        }

        var normalisedBaseUrl = baseUrl.copy(path = Path(basePathParts.joinToString("/")))

        if (baseUrl.host.toString().startsWith("ssh.")) {
            normalisedBaseUrl = normalisedBaseUrl.copy(host = Host(baseUrl.host.toString().removePrefix("ssh.")))
        }
        return normalisedBaseUrl
    }
}