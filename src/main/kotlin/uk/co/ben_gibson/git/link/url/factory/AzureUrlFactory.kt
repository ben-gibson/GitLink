package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.*
import uk.co.ben_gibson.url.*

@Service
class AzureUrlFactory: UrlFactory {
    private val host = Host("dev.azure.com")

    override fun createUrl(options: UrlOptions): URL {
        val basePathParts = options.baseUrl.path.toString()
            .split("/")
            .toMutableList()

        // Azure expects this to be in the path between the project and repo name. It's already included when cloning the project using HTTPS, but not when cloning the project using SSH.
        if (!basePathParts.contains("_git")) {
            basePathParts.add(2, "_git")
        }
        
        val baseUrl = URL(scheme = Scheme.https(), host = host, path = Path(basePathParts.joinToString("/")))

        return when (options) {
            is UrlOptionsFileAtBranch -> createUrlToFileAtBranch(baseUrl, options)
            is UrlOptionsFileAtCommit -> createUrlToFileAtCommit(baseUrl, options)
            is UrlOptionsCommit -> createUrlToCommit(baseUrl, options)
        }
    }

    private fun createUrlToFileAtCommit(baseUrl: URL, options: UrlOptionsFileAtCommit) : URL {
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

    private fun createUrlToFileAtBranch(baseUrl: URL, options: UrlOptionsFileAtBranch) : URL {
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

    private fun createUrlToCommit(baseUrl: URL, options: UrlOptionsCommit) = baseUrl
        .withPath(Path.fromSegments(listOf("commit", options.commit.toString())))

    private fun addLineSelectionParameters(queryString: QueryString, lineSelection: LineSelection) : QueryString {
        return queryString
            .withParameter("line", lineSelection.start.toString())
            .withParameter("lineEnd", (lineSelection.end + 1).toString())

            // TODO: Pass column selection in.
            .withParameter("lineStartColumn", "1")
            .withParameter("lineEndColumn", "1")
    }
}