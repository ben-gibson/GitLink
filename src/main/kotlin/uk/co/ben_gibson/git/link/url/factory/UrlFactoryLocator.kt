package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.git.Host

@Service
class UrlFactoryLocator(private val project: Project) {
    fun locate(remoteHost: Host) : UrlFactory {
        return when(remoteHost) {
            Host.GIT_HUB -> service<GitHub>()
            Host.GITLAB -> service<GitLab>()
            Host.BITBUCKET_SERVER -> service<BitbucketServer>()
            Host.BITBUCKET_CLOUD -> service<BitbucketCloud>()
            Host.GITEA -> service<Gogs>()
            Host.GOGS -> service<Gogs>()
            Host.AZURE -> service<Azure>()
            Host.CUSTOM -> project.service<CustomUrlFactory>()
        }
    }
}