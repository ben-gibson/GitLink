package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.url.*
import uk.co.ben_gibson.url.Host
import uk.co.ben_gibson.url.Path
import uk.co.ben_gibson.url.URL

private val HOST = Host.create("source.chromium.org")

private const val IDENTIFIER_CHROMIUMOS = "chromiumos"

@Service
class ChromiumUrlFactory: UrlFactory {
    override fun createUrl(options: UrlOptions): URL {
        val path = if (options.baseUrl.path.toString().contains(IDENTIFIER_CHROMIUMOS))
            createPathForChromiumos(options)
        else
            createPathForChromium(options)

        return URL(scheme = options.baseUrl.scheme, host = HOST, path = Path.create(path))
    }

    private fun createPathForChromium(options: UrlOptions) : String {
        val pathBuilder = PathBuilder()

        pathBuilder
            .withPart("chromium")
            .withSubPath(options.baseUrl.path.toString())
            .withPart("+")

        when (options) {
            is UrlOptionsFileAtBranch -> pathBuilder.withSubPath(createChromiumFileSubPath(options))
            is UrlOptionsFileAtCommit -> pathBuilder.withSubPath(createChromiumFileSubPath(options))
            is UrlOptionsCommit -> pathBuilder.withPart(options.commit.toString())
        }

        return pathBuilder.build()
    }

    private fun createPathForChromiumos(options: UrlOptions) : String {
        val pathBuilder = PathBuilder()

        when (options) {
            is UrlOptionsFileAtBranch -> pathBuilder.withSubPath(createChromiumosFileSubPath(options))
            is UrlOptionsFileAtCommit -> pathBuilder.withSubPath(createChromiumosFileSubPath(options))
            is UrlOptionsCommit -> pathBuilder
                .withSubPath("chromiumos/_/chromium/chromiumos")
                .withParts(options.baseUrl.path.toString().split('/').filter{ it.isNotBlank() }.drop(1))
                .withPart("+")
                .withPart(options.commit.toString())
        }

        return pathBuilder.build()
    }

    private fun createChromiumFileSubPath(options: UrlOptionsFileAware): String {
        var path = PathBuilder()
            .withParts("${options.ref}:".plus(options.file.path.trim('/')).split("/"))
            .withPart(if (options.file.isRoot) "" else options.file.name)
            .build()

        if (options.file.isDirectory) {
            return path
        }

        return path.plus(createLineSelection(options))
    }

    private fun createChromiumosFileSubPath(options: UrlOptionsFileAware): String {
        val path = PathBuilder()
            .withSubPath("chromiumos/chromiumos/codesearch")
            .withPart("+")
            .withPart(options.ref.plus(":src"))
            .withParts(options.baseUrl.path.toString().split('/').filter{ it.isNotBlank() }.drop(1))
            .withParts(options.file.path.split("/").filter { it.isNotBlank() })
            .withPart(if (options.file.isRoot) "" else options.file.name)
            .build()

        if (options.file.isDirectory) {
            return path
        }

        return path.plus(createLineSelection(options))
    }

    private fun createLineSelection(options: UrlOptionsFileAware) : String? {
        val selection = options.lineSelection ?: return ""
        return ";l=${selection.start}-${selection.end}"
    }
}