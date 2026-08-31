package uk.co.ben_gibson.git.link.platform

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.util.messages.Topic
import uk.co.ben_gibson.git.link.settings.ApplicationSettings
import uk.co.ben_gibson.git.link.settings.ApplicationSettings.CustomPlatformSettings
import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import uk.co.ben_gibson.url.Host
import java.util.UUID

private val EXISTING_PLATFORMS = setOf(
    GitHub(),
    GitLab(),
    BitbucketCloud(),
    BitbucketServer(),
    Gitee(),
    Gitea(),
    Gogs(),
    Srht(),
    Azure(),
    Gerrit(),
    Codeberg()
)

@Service
class PlatformRepository {
    fun getById(id: String) = getById(UUID.fromString(id))
    fun getById(id: UUID) = load().firstOrNull { it.id == id }
    fun getAll() = load()

    fun getCustomPlatforms(): List<CustomPlatform> = service<ApplicationSettings>()
        .customPlatforms
        .map { it.toCustomPlatform() }

    fun saveCustomPlatforms(customPlatforms: List<CustomPlatform>) {
        if (customPlatforms == getCustomPlatforms()) {
            return
        }

        service<ApplicationSettings>().customPlatforms = customPlatforms.map { it.toSettings() }

        ApplicationManager.getApplication()
            .messageBus
            .syncPublisher(ChangeListener.TOPIC)
            .onChange()
    }

    /**
     * The platform serving a domain, e.g. github.com, searched for in the order:
     *
     * 1. A user registered domain against a platform, which is an explicit choice and so beats
     *    anything built-in.
     * 2. An exact match on a domain, so bitbucket.org resolves to Bitbucket Cloud
     *    before Bitbucket Server which uses '*bitbucket*'.
     * 3. A wildcard, e.g. '*gerrit*'.
     */
    fun getByDomain(host: Host): Platform? {
        val platforms = load()

        return getByRegisteredDomain(host)
            ?: platforms.firstOrNull { it.matchesExactly(host) }
            ?: platforms.firstOrNull { it.matches(host) }
    }

    private fun getByRegisteredDomain(host: Host) = service<ApplicationSettings>()
        .registeredDomains
        .entries
        .firstOrNull { (_, domains) -> domains.any { Domain.of(it).matches(host) } }
        ?.let { getById(it.key) }

    private fun load(): Set<Platform> = EXISTING_PLATFORMS.plus(getCustomPlatforms().map { Custom(it) })

    private fun CustomPlatformSettings.toCustomPlatform() = CustomPlatform(
        UUID.fromString(id),
        displayName,
        Domain.of(baseUrl),
        UrlTemplates(fileAtBranchTemplate, fileAtCommitTemplate, commitTemplate)
    )

    private fun CustomPlatform.toSettings() = CustomPlatformSettings(
        id.toString(),
        name,
        domain.toString(),
        templates.fileAtBranch,
        templates.fileAtCommit,
        templates.commit
    )

    fun interface ChangeListener {
        fun onChange()

        companion object {
            @Topic.AppLevel
            @JvmField
            val TOPIC: Topic<ChangeListener> = Topic(ChangeListener::class.java, Topic.BroadcastDirection.NONE)
        }
    }
}
