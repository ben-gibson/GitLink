package uk.co.ben_gibson.git.link

import com.intellij.DynamicBundle
import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey
import java.net.URL

@NonNls
private const val BUNDLE = "messages.MyBundle"

object GitLinkBundle : DynamicBundle(BUNDLE) {
    public val URL_BUG_REPORT = URL("https://github.com/ben-gibson/GitLink/issues")

    @Suppress("SpreadOperator")
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getMessage(key, *params)

    @Suppress("SpreadOperator", "unused")
    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getLazyMessage(key, *params)

    fun openPluginSettings(project: Project) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, message("settings.general.group.title"))
    }

    fun openRepository() {
        BrowserLauncher.instance.open("https://github.com/ben-gibson/GitLink");
    }

    fun openHostPoll() {
        BrowserLauncher.instance.open("https://github.com/ben-gibson/GitLink/discussions/172");
    }

    fun plugin() = PluginManagerCore.getPlugin(PluginId.getId("uk.co.ben-gibson.remote.repository.mapper"))
}
