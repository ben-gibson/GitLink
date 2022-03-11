package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.url.UrlOptions
import java.net.URL

@Service
class CustomUrlFactory(project: Project) : UrlFactory {
    override fun createUrl(options: UrlOptions): URL {
        TODO("Not yet implemented")
    }
}