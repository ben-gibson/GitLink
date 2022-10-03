package uk.co.ben_gibson.git.link.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Supports storing the application settings in a persistent way.
 * The [State] and [Storage] annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(name = "uk.co.ben_gibson.git.link.SettingsState", storages = [Storage("GitLink.xml")])
class ProjectSettings : PersistentStateComponent<ProjectSettings?> {
    var host: String? = null
    var fallbackBranch = "main"
    var remote = "origin"
    var shouldCheckRemote = true
    var forceHttps = true
    var showPerformanceTip = true

    override fun getState() = this

    override fun loadState(state: ProjectSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
