package uk.co.ben_gibson.git.link

import com.intellij.DynamicBundle
import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.ide.plugins.cl.PluginAwareClassLoader
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "messages.MyBundle"

object GitLinkBundle : DynamicBundle(BUNDLE) {
    const val URL_BUG_REPORT = "https://github.com/ben-gibson/GitLink/issues"

    @Suppress("SpreadOperator")
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getMessage(key, *params)

    fun openPluginSettings(project: Project) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, message("settings.general.group.title"))
    }

    fun openRepository() {
        BrowserLauncher.instance.open("https://github.com/ben-gibson/GitLink")
    }

    fun openReview() {
        BrowserLauncher.instance.open("https://plugins.jetbrains.com/plugin/8183-gitlink/reviews")
    }

    // The plugin class loader knows its own descriptor, which avoids the internal plugin manager lookups.
    fun plugin() = (GitLinkBundle::class.java.classLoader as? PluginAwareClassLoader)?.pluginDescriptor
}
