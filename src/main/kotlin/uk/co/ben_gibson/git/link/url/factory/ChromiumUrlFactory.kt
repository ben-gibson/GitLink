package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.ui.LineSelection
import uk.co.ben_gibson.git.link.url.*
import uk.co.ben_gibson.url.Host
import uk.co.ben_gibson.url.Path
import uk.co.ben_gibson.url.URL

private val HOST = Host("source.chromium.org")

private const val IDENTIFIER_CHROMIUMOS = "chromiumos"

@Service
class ChromiumUrlFactory: UrlFactory {
    override fun createUrl(options: UrlOptions): URL {
        val path = if (options.baseUrl.path.toString().contains(IDENTIFIER_CHROMIUMOS))
            createPathForChromiumos(options)
        else
            createPathForChromium(options)

        return URL(scheme = options.baseUrl.scheme, host = HOST, path = path)
    }

    private fun createPathForChromium(options: UrlOptions) : Path {
        val path = Path("chromium")
            .with(Path(options.baseUrl.path.toString()))
            .with(Path("+"))

        return when (options) {
            is UrlOptionsFileAtBranch -> path.with(createChromiumFileSubPath(options))
            is UrlOptionsFileAtCommit -> path.with(createChromiumFileSubPath(options))
            is UrlOptionsCommit -> path.with(Path(options.commit.toString()))
        }
    }

    private fun createPathForChromiumos(options: UrlOptions) : Path {
        return when (options) {
            is UrlOptionsFileAtBranch -> createChromiumosFileSubPath(options)
            is UrlOptionsFileAtCommit -> createChromiumosFileSubPath(options)
            is UrlOptionsCommit -> Path("chromiumos/_/chromium/chromiumos")
                .withSegments(options.baseUrl.path.toString().split('/').filter{ it.isNotBlank() }.drop(1))
                .with(Path("+"))
                .with(Path(options.commit.toString()))
        }
    }

    private fun createChromiumFileSubPath(options: UrlOptionsFileAware): Path {
        var path = Path.fromSegments("${options.ref}:".plus(options.file.path.trim('/')).split("/"))

        if (!options.file.isRoot) {
            path = path.withSegment(options.file.name)
        }

        if (options.file.isDirectory) {
            return path
        }

        val lineSelection = options.lineSelection ?: return path

        return Path(path.toString() + createLineSelection(lineSelection))
    }

    private fun createChromiumosFileSubPath(options: UrlOptionsFileAware): Path {
        var path = Path("chromiumos/chromiumos/codesearch")
            .with("+")
            .with(options.ref.plus(":src"))
            .withSegments(options.baseUrl.path.toString().split('/').filter{ it.isNotBlank() }.drop(1))
            .withSegments(options.file.path.split("/").filter { it.isNotBlank() })

        if (!options.file.isRoot) {
            path = path.withSegment(options.file.name)
        }

        if (options.file.isDirectory) {
            return path
        }

        val lineSelection = options.lineSelection ?: return path

        return Path(path.toString() + createLineSelection(lineSelection))
    }

    private fun createLineSelection(selection: LineSelection) = ";l=${selection.start}-${selection.end}"
}