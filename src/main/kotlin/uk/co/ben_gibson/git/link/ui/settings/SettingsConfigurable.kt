package uk.co.ben_gibson.git.link.ui.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import uk.co.ben_gibson.git.link.SettingsState
import javax.swing.JComponent

class SettingsConfigurable(private val project : Project) : Configurable {
    private var mySettingsComponent: SettingsComponent? = null

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP
    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String? {
        return "SDK: Application Settings Example"
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return mySettingsComponent!!.preferredFocusedComponent
    }

    override fun createComponent(): JComponent? {
        mySettingsComponent = SettingsComponent()

        return mySettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = SettingsState.getInstance(project)

        var modified = mySettingsComponent!!.userNameText != settings.userId

        modified = modified or (mySettingsComponent!!.ideaUserStatus != settings.ideaStatus)

        return modified
    }

    override fun apply() {
        val settings = SettingsState.getInstance(project)
        settings.userId = mySettingsComponent!!.userNameText
        settings.ideaStatus = mySettingsComponent!!.ideaUserStatus
    }

    override fun reset() {
        val settings = SettingsState.getInstance(project)

        mySettingsComponent!!.userNameText = settings.userId
        mySettingsComponent!!.ideaUserStatus = settings.ideaStatus
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}