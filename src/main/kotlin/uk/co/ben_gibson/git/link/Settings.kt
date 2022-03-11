package uk.co.ben_gibson.git.link

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import uk.co.ben_gibson.git.link.git.RemoteHost

/**
 * Supports storing the application settings in a persistent way.
 * The [State] and [Storage] annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(name = "uk.co.ben_gibson.git.link.SettingsState", storages = [Storage("GitLink.xml")])
class Settings : PersistentStateComponent<Settings?> {
    var remoteHost = RemoteHost.GITLAB
    var defaultBranch = "master"
    var remote = "origin"
    var checkCommitOnRemote = true
    var forceHttps = true

    override fun getState() = this

    override fun loadState(state: Settings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}