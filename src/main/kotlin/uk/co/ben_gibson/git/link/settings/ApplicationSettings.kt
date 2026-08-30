package uk.co.ben_gibson.git.link.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.OptionTag
import com.intellij.util.xmlb.annotations.Tag
import java.util.UUID

@State(name = "uk.co.ben_gibson.git.link.SettingsState", storages = [Storage("GitLink.xml")])
class ApplicationSettings : PersistentStateComponent<ApplicationSettings?> {
    private var listeners: List<ChangeListener> = listOf()

    @get:OptionTag("customHosts")
    var customPlatforms: List<CustomPlatformSettings> = listOf()
        set(value) {
            field = value
            notifyListeners()
        }

    @get:OptionTag("customHostDomains")
    var registeredDomains: Map<String, Set<String>> = mapOf()

    var lastVersion: String? = null
    var hits = 0
    var requestSupport = true

    override fun getState() = this

    override fun loadState(state: ApplicationSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    @Tag("custom_hosts")
    data class CustomPlatformSettings(
        var id: String = UUID.randomUUID().toString(),
        var displayName: String = "",
        var baseUrl: String = "",
        var fileAtBranchTemplate: String = "",
        var fileAtCommitTemplate: String = "",
        var commitTemplate: String = ""
    )

    fun registerListener(listener: ChangeListener) {
        listeners = listeners.plus(listener)
    }

    fun recordHit() {
        hits++
    }

    private fun notifyListeners() {
        listeners.forEach(ChangeListener::onChange)
    }

    interface ChangeListener {
        fun onChange()
    }
}
