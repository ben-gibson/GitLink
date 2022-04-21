package uk.co.ben_gibson.git.link.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Tag;
import java.util.UUID

/**
 * Supports storing the application settings in a persistent way.
 * The [State] and [Storage] annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(name = "uk.co.ben_gibson.git.link.SettingsState", storages = [Storage("GitLink.xml")])
class ApplicationSettings : PersistentStateComponent<ApplicationSettings?> {
    private var listeners: MutableList<ChangeListener> = mutableListOf()

    var customHosts: MutableList<CustomHostSettings> = mutableListOf()
        set(value) {
            field = value
            notifyListeners()
        }

    var lastVersion: String? = null

    var hits = 0

    var requestSupport = true;

    override fun getState() = this

    override fun loadState(state: ApplicationSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    @Tag("custom_hosts")
    data class CustomHostSettings(
        var id: String = UUID.randomUUID().toString(),
        var displayName: String = "",
        var baseUrl: String = "",
        var fileAtBranchTemplate: String = "",
        var fileAtCommitTemplate: String = "",
        var commitTemplate: String = ""
    )

    fun registerListener(listener: ChangeListener) {
        listeners.add(listener)
    }

    fun incrementHits() {
        hits++
    }

    private fun notifyListeners() {
        listeners.forEach(ChangeListener::onChange)
    }

    interface ChangeListener {
        fun onChange()
    }
}