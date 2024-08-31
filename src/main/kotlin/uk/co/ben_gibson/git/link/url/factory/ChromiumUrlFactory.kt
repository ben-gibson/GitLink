package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.*
import uk.co.ben_gibson.url.Host
import uk.co.ben_gibson.url.Path
import uk.co.ben_gibson.url.URL

private val HOST = Host("source.chromium.org")

private const val IDENTIFIER_CHROMIUMOS = "chromiumos"

@Service
class ChromiumUrlFactory: UrlFactory {
    override fun createUrl(baseUrl: URL, options: UrlOptions): URL {
        val path = if (baseUrl.path.toString().contains(IDENTIFIER_CHROMIUMOS))
            createPathForChromiumos(baseUrl, options)
        else
            createPathForChromium(baseUrl, options)

        return URL(scheme = baseUrl.scheme, host = HOST, path = path)
    }

    private fun createPathForChromium(baseUrl: URL, options: UrlOptions) : Path {
        val path = Path("chromium")
            .with(Path(baseUrl.path.toString()))
            .with(Path("+"))

        return when (options) {
            is UrlOptions.UrlOptionsFileAtBranch -> path.with(createChromiumFileSubPath(options.file, options.branch, options.lineSelection))
            is UrlOptions.UrlOptionsFileAtCommit -> path.with(createChromiumFileSubPath(options.file, options.commit.toString(), options.lineSelection))
            is UrlOptions.UrlOptionsCommit -> path.with(Path(options.commit.toString()))
        }
    }

    private fun createPathForChromiumos(baseUrl: URL, options: UrlOptions) : Path {
        return when (options) {
            is UrlOptions.UrlOptionsFileAtBranch -> createChromiumosFileSubPath(baseUrl, options.file, options.branch, options.lineSelection)
            is UrlOptions.UrlOptionsFileAtCommit -> createChromiumosFileSubPath(baseUrl, options.file, options.commit.toString(), options.lineSelection)
            is UrlOptions.UrlOptionsCommit -> Path("chromiumos/_/chromium/chromiumos")
                .withSegments(baseUrl.path.toString().split('/').filter{ it.isNotBlank() }.drop(1))
                .with(Path("+"))
                .with(Path(options.commit.toString()))
        }
    }

    private fun createChromiumFileSubPath(file: File, ref: String, lineSelection: LineSelection?): Path {

        var path = Path.fromSegments("${ref}:".plus(file.path.trim('/')).split("/"))

        if (!file.isRoot) {
            path = path.withSegment(file.name)
        }

        if (file.isDirectory) {
            return path
        }

        lineSelection ?: return path

        return Path(path.toString() + createLineSelection(lineSelection))
    }

    private fun createChromiumosFileSubPath(baseUrl: URL, file: File, ref: String, lineSelection: LineSelection?): Path {
        var path = Path("chromiumos/chromiumos/codesearch")
            .with("+")
            .with(ref.plus(":src"))
            .withSegments(baseUrl.path.toString().split('/').filter{ it.isNotBlank() }.drop(1))
            .withSegments(file.path.split("/").filter { it.isNotBlank() })

        if (!file.isRoot) {
            path = path.withSegment(file.name)
        }

        if (file.isDirectory) {
            return path
        }

        lineSelection ?: return path

        return Path(path.toString() + createLineSelection(lineSelection))
    }

    private fun createLineSelection(selection: LineSelection) = ";l=${selection.start}-${selection.end}"
}