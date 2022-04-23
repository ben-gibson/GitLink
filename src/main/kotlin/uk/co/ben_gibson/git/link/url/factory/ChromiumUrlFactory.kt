package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.url.*
import java.net.URL

private const val HOST = "source.chromium.org"

private const val IDENTIFIER_CHROMIUMOS = "chromiumos"

@Service
class ChromiumUrlFactory: UrlFactory {
    override fun createUrl(options: UrlOptions): URL {
        var path = if (options.baseUrl.path.contains(IDENTIFIER_CHROMIUMOS))
            createPathForChromiumos(options)
        else
            createPathForChromium(options)

        path = path.plus(createLineSelectionSubPath(options) ?: "")

        return URL(options.baseUrl.protocol, HOST, path)
    }

    private fun createPathForChromium(options: UrlOptions) : String {
        val pathBuilder = PathBuilder()

        pathBuilder
            .withPart("chromium")
            .withSubPath(options.baseUrl.path)
            .withPart("+")

        when (options) {
            is UrlOptionsFileAtBranch -> pathBuilder.withSubPath(createChromiumFileSubPath(options.file, options.branch))
            is UrlOptionsFileAtCommit -> pathBuilder.withSubPath(createChromiumFileSubPath(options.file, options.commit.toString()))
            is UrlOptionsCommit -> pathBuilder.withPart(options.commit.toString())
        }

        return pathBuilder.build()
    }

    private fun createPathForChromiumos(options: UrlOptions) : String {
        val pathBuilder = PathBuilder()

        when (options) {
            is UrlOptionsFileAtBranch -> pathBuilder.withSubPath(createChromiumosFileSubPath(options.baseUrl, options.file, options.branch))
            is UrlOptionsFileAtCommit -> pathBuilder.withSubPath(createChromiumosFileSubPath(options.baseUrl, options.file, options.commit.toString()))
            is UrlOptionsCommit -> pathBuilder
                .withSubPath("chromiumos/_/chromium/chromiumos")
                .withParts(options.baseUrl.path.split('/').filter{ it.isNotBlank() }.drop(1))
                .withPart("+")
                .withPart(options.commit.toString())
        }

        return pathBuilder.build()
    }

    private fun createChromiumFileSubPath(file: File, ref: String) = PathBuilder()
        .withParts("${ref}:".plus(file.path).split("/"))
        .withPart(if (file.isRoot) "" else file.name)
        .build()

    private fun createChromiumosFileSubPath(baseUrl: URL, file: File, ref: String) = PathBuilder()
        .withSubPath("chromiumos/chromiumos/codesearch")
        .withPart("+")
        .withPart(ref.plus(":src"))
        .withParts(baseUrl.path.split('/').filter{ it.isNotBlank() }.drop(1))
        .withParts(file.path.split("/").filter { it.isNotBlank() })
        .withPart(if (file.isRoot) "" else file.name)
        .build()

    private fun createLineSelectionSubPath(options: UrlOptions) : String? {
        if (options !is LineSelectionAware) return null

        return options.lineSelection?.let { ";l=${it.start}-${it.end}" }
    }
}