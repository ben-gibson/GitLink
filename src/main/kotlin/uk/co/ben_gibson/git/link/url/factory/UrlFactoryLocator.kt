package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.git.RemoteHost

@Service
class UrlFactoryLocator(private val project: Project) {
    fun locateFactory(remoteHost: RemoteHost) : UrlFactory {
        return when(remoteHost) {
            RemoteHost.GIT_HUB -> service<GitHubUrlFactory>()
            RemoteHost.GITLAB -> service<GitHubUrlFactory>()
            RemoteHost.BITBUCKET_SERVER -> service<GitHubUrlFactory>()
            RemoteHost.BITBUCKET_CLOUD -> service<GitHubUrlFactory>()
            RemoteHost.GITBLIT -> service<GitHubUrlFactory>()
            RemoteHost.GITEA -> service<GitHubUrlFactory>()
            RemoteHost.GOGS -> service<GitHubUrlFactory>()
            RemoteHost.AZURE -> service<GitHubUrlFactory>()
            RemoteHost.CUSTOM -> project.service<CustomUrlFactory>()
        }
    }
}